<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width">
        <meta name="viewport" content="initial-scale=1">
        <meta name="viewport" content="user-scalable=no">
        <meta charset="utf-8">
        <link rel="stylesheet" href="/static/assets/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="/static/assets/spring-cloud-health.css">
        <script src="/static/assets/jquery/jquery.min.js"></script>
        <script src="/static/assets/bootstrap/js/bootstrap.min.js"></script>
        <script src="/static/assets/echarts/echarts.min.js"></script>
    </head>
    <body>
        <div id="cont">
            <div class="header">
                <ul class="nav nav-pills">
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                            ${selected?upper_case}
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                        <#list services as service>
                            <li>${service?upper_case}</li>
                        </#list>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </body>
</html>