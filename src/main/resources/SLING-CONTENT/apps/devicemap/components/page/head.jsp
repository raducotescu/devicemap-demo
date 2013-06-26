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
<%@page session="false" contentType="text/html; UTF-8" %>
<%@include file="global.jsp"%>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width">
    <title>Apache DeviceMap Demo Page</title>
    <link rel="stylesheet" type="text/css" href="/content/demo/css/style.css">
    <c:if test="${imageSelector == '' || imageSelector == 'tablet'}">
    <script src="/content/demo/js/jquery-1.10.1.min.js" type="text/javascript"></script>
    <style>
        html {
            background: url("/content/demo/img/clown.jpg") no-repeat center center fixed;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }
        body {
            color: #ffffff;
        }
        #details {
            background-color: rgba(100, 100, 100, 0.5);
            min-height: 380px;
            padding: 10px;
            margin-bottom: 30px;
        }
    </style>
    <script type="text/javascript">
        ((function($) {
            function setHeight() {
                var height = 0;
                $('#details > div').each(function() {
                    var elHeight = $(this).height();
                    if (elHeight > height) {
                        height = elHeight;

                    }
                });
                $('#details').height(height);
            }

            $(window).on('resize', function() {
                setHeight();
            })

            $(window).on('load', function() {
                setHeight();
            })

        })($));
    </script>
    </c:if>
</head>
