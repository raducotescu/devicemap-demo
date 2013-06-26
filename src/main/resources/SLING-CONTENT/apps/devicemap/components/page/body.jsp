<%--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  --%>
<%@page session="false" contentType="text/html; UTF-8"
        import="org.apache.sling.devicemap.api.DeviceProperty"%>
<%@include file="global.jsp"%>
<body>
    <div id="result">
        <p>The Apache DeviceMap server-side client detection module thinks your device is a
            <span><%= deviceType %></span>.</p>
    </div>
    <div id="details">
        <div id="description" <c:if test="${imageSelector == '' || imageSelector == 'tablet'}"> style="float: left; width: 50%"</c:if>>
            <p>Based on your browser's <code>User-Agent</code> header, the following properties have been identified:</p>
            <ol>
                <li>vendor: <%= capabilities.get(DeviceProperty.VENDOR) %></li>
                <li>model: <%= capabilities.get(DeviceProperty.MODEL) %></li>
                <li>marketing name: <%= capabilities.get(DeviceProperty.MARKETING_NAME) %></li>
                <li>input devices: <%= capabilities.get(DeviceProperty.INPUT_DEVICES) %></li>
                <li>screen width: <%= capabilities.get(DeviceProperty.DISPLAY_WIDTH) %></li>
                <li>screen height: <%= capabilities.get(DeviceProperty.DISPLAY_HEIGHT) %></li>
            </ol>
        </div>
        <c:if test="${imageSelector == '' || imageSelector == 'tablet'}">
        <div id="desktop-content" style="float: right; width: 50%">
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec dapibus, ipsum vitae fermentum sollicitudin, eros leo sodales nibh,
                sed varius nulla felis nec quam. Quisque blandit est eget enim suscipit, a cursus libero consequat. Pellentesque ac malesuada ipsum.
                Duis interdum nec felis in eleifend. Proin porttitor mattis pharetra. Sed eu semper magna, mattis blandit dui. Praesent in fermentum
                orci. Donec ultrices, nulla quis congue tempor, nibh enim suscipit nulla, tempor condimentum odio augue a mi. Maecenas quis leo nec
                neque tincidunt auctor et ut elit. Fusce aliquam tellus in massa porta tristique. Curabitur quis tristique erat, et luctus sem.
                Vivamus suscipit iaculis tortor quis tempus. Aenean pellentesque elementum erat, sed lacinia dui eleifend sed. Praesent sit amet
                felis et odio pharetra pharetra eget et felis. Etiam adipiscing purus quam, quis porttitor nulla sollicitudin sed. Donec sit amet
                purus ac neque fermentum tristique.</p>

            <p>Integer vitae dolor varius, consequat lorem sit amet, aliquam risus. Nunc facilisis accumsan urna sed pulvinar. Fusce sollicitudin
                tincidunt leo vel congue. Curabitur fermentum, sapien eget posuere commodo, dolor libero vehicula orci, sit amet consectetur leo
                dolor id nisl. Cras diam sem, pharetra at lobortis et, vulputate et nibh. Phasellus nisi nunc, eleifend sed nulla quis, tempor
                posuere ante. Aenean rutrum purus non odio rutrum, euismod fringilla felis interdum.</p>
        </div>
        </c:if>
    </div>
</body>
