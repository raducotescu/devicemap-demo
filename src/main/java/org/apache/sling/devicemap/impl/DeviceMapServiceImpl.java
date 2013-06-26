package org.apache.sling.devicemap.impl;

import org.apache.devicemap.simpleddr.ODDRService;
import org.apache.devicemap.simpleddr.model.ODDRHTTPEvidence;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.devicemap.api.DeviceMapService;
import org.apache.sling.devicemap.api.DeviceProperty;
import org.apache.sling.devicemap.api.DeviceType;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.ddr.simple.Evidence;
import org.w3c.ddr.simple.PropertyRef;
import org.w3c.ddr.simple.PropertyValue;
import org.w3c.ddr.simple.PropertyValues;
import org.w3c.ddr.simple.ServiceFactory;
import org.w3c.ddr.simple.exception.NameException;
import org.w3c.ddr.simple.exception.ValueException;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(metatype = true)
@Service(DeviceMapService.class)
@Properties({
        @Property(
                name = DeviceMapServiceImpl.SCR_PROP_NAME_DATAFILES_LOCATION,
                value = DeviceMapServiceImpl.SCR_PROP_DEFAULT_DATAFILES_LOCATION,
                propertyPrivate = true
        )
})
public class DeviceMapServiceImpl implements DeviceMapService {

    public static final String SCR_PROP_DEFAULT_DATAFILES_LOCATION = "/apps/devicemap/datafiles";
    public static final String SCR_PROP_NAME_DATAFILES_LOCATION = "org.apache.sling.devicemap.datafiles.location";

    private static final Logger LOG = LoggerFactory.getLogger(DeviceMapServiceImpl.class);
    private String datafilesLocation;
    private org.w3c.ddr.simple.Service idService;

    @Reference
    private ResourceResolverFactory rrf;

    @Override
    public Map<DeviceProperty, Object> getCapabilities(String userAgent) {
        Map<DeviceProperty, Object> capabilities = new HashMap<DeviceProperty, Object>();
        try {
            PropertyRef vendor_Ref = idService.newPropertyRef("vendor");
            PropertyRef model_Ref = idService.newPropertyRef("model");
            PropertyRef is_wireless_device_Ref = idService.newPropertyRef("is_wireless_device");
            PropertyRef is_tablet_Ref = idService.newPropertyRef("is_tablet");
            PropertyRef displayWidth_Ref = idService.newPropertyRef("displayWidth");
            PropertyRef displayHeight_Ref = idService.newPropertyRef("displayHeight");
            PropertyRef marketing_name_Ref = idService.newPropertyRef("marketing_name");
            PropertyRef inputDevices_Ref = idService.newPropertyRef("inputDevices");
            PropertyRef[] propertyRefs = new PropertyRef[] {vendor_Ref, model_Ref, is_wireless_device_Ref, is_tablet_Ref,
                   displayWidth_Ref, displayHeight_Ref, marketing_name_Ref, inputDevices_Ref};

            Evidence evidence = new ODDRHTTPEvidence();
            evidence.put(Constants.USER_AGENT_HEADER, userAgent);

            PropertyValues propertyValues = idService.getPropertyValues(evidence, propertyRefs);
            PropertyValue vendor = propertyValues.getValue(vendor_Ref);
            PropertyValue model = propertyValues.getValue(model_Ref);
            PropertyValue is_wireless = propertyValues.getValue(is_wireless_device_Ref);
            PropertyValue is_tablet = propertyValues.getValue(is_tablet_Ref);
            PropertyValue displayWidth = propertyValues.getValue(displayWidth_Ref);
            PropertyValue displayHeight = propertyValues.getValue(displayHeight_Ref);
            PropertyValue marketingName = propertyValues.getValue(marketing_name_Ref);
            PropertyValue inputDevices = propertyValues.getValue(inputDevices_Ref);

            if (vendor.exists()) {
                capabilities.put(DeviceProperty.VENDOR, vendor.getString());
            }
            if (model.exists()) {
                capabilities.put(DeviceProperty.MODEL, model.getString());
            }
            if (displayHeight.exists()) {
                capabilities.put(DeviceProperty.DISPLAY_HEIGHT, displayHeight.getInteger());
            }
            if (displayWidth.exists()) {
                capabilities.put(DeviceProperty.DISPLAY_WIDTH, displayWidth.getInteger());
            }
            if (marketingName.exists()) {
                capabilities.put(DeviceProperty.MARKETING_NAME, marketingName.getString());
            }
            if (inputDevices.exists()) {
                capabilities.put(DeviceProperty.INPUT_DEVICES, inputDevices.getString());
            }
            capabilities.put(DeviceProperty.DEVICE_TYPE, getDeviceType(is_wireless, is_tablet));
        } catch (NameException e) {
            LOG.error("Unknown property while identifying device " + userAgent, e);
        } catch (ValueException e) {
            LOG.error("Value exception while identifying device " + userAgent, e);
        }
        return capabilities;
    }

    private DeviceType getDeviceType(PropertyValue is_wireless, PropertyValue is_tablet) {
        DeviceType type = DeviceType.DESKTOP;
        try {
            if (is_tablet.exists() && is_tablet.getBoolean()) {
                type = DeviceType.TABLET;
            } else if (is_wireless.exists() && is_wireless.getBoolean()) {
                type = DeviceType.SMARTPHONE;
            }
        } catch (ValueException e) {
            LOG.error("Cannot retrieve device type.", e);
        }
        return type;
    }

    // OSGi ################################################################################################################################
    protected void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();
        datafilesLocation = PropertiesUtil.toString(properties.get(SCR_PROP_NAME_DATAFILES_LOCATION), SCR_PROP_DEFAULT_DATAFILES_LOCATION);
        java.util.Properties idServiceProps = new java.util.Properties();
        try {
            ResourceResolver rr = rrf.getAdministrativeResourceResolver(null);
            Resource dataFilesLocationResource = rr.getResource(datafilesLocation);
            if (dataFilesLocationResource != null) {
                for (Iterator<Resource> it = dataFilesLocationResource.listChildren(); it.hasNext(); ) {
                    Resource child = it.next();
                    String name = child.getName();
                    if ("BuilderDataSource.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_UA_DEVICE_BUILDER_STREAM, child.adaptTo(InputStream.class));
                    } else if ("BuilderDataSourcePatch.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_UA_DEVICE_BUILDER_PATCH_STREAMS, new InputStream[]{child.adaptTo
                                (InputStream.class)});
                    } else if ("DeviceDataSource.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_UA_DEVICE_DATASOURCE_STREAM, child.adaptTo(InputStream.class));
                    } else if ("DeviceDataSourcePatch.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_UA_DEVICE_DATASOURCE_PATCH_STREAMS, new InputStream[]{child.adaptTo(InputStream
                                .class)});
                    } else if ("BrowserDataSource.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_UA_BROWSER_DATASOURCE_STREAM, child.adaptTo(InputStream.class));
                    } else if ("OperatingSystemDataSource.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_UA_OPERATINGSYSTEM_DATASOURCE_STREAM, child.adaptTo(InputStream.class));
                    } else if ("coreVocabulary.xml".equals(name)) {
                        idServiceProps.put(Constants.DDR_CORE_VOCABULARY_STREAM, child.adaptTo(InputStream.class));
                    } else if ("oddrVocabulary.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_VOCABULARY_STREAM, new InputStream[] {child.adaptTo(InputStream.class)});
                    } else if ("oddrLimitedVocabulary.xml".equals(name)) {
                        idServiceProps.put(Constants.ODDR_LIMITED_VOCABULARY_STREAM, child.adaptTo(InputStream.class));
                    }
                }
            }
            idServiceProps.put(Constants.ODDR_VOCABULARY_DEVICE, "http://www.openddr.org/oddr-vocabulary");
            idService = ServiceFactory.newService(ODDRService.class.getName(),
                    idServiceProps.getProperty(Constants.ODDR_VOCABULARY_DEVICE), idServiceProps);
        } catch (LoginException e) {
            LOG.error("Cannot obtain a resource resolver!", e);
        } catch (Exception e) {
            LOG.error("Cannot initialise the DeviceMapClient.", e);
        }
    }
}
