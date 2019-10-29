<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- CSRF Token -->
    <meta name="csrf-token" content="{{ csrf_token() }}">
    
    <title>Discover your Purpose</title>
    <link rel="shortcut icon" href="{{ url('/favicon.ico') }}"
        type="image/x-icon">
    <link rel="icon" href="{{ url('/favicon.ico') }}" type="image/x-icon">
    <link href="{{ url('/css/app.css') }}" rel="stylesheet">
    <link href="{{ url('/css/sidebar-toggle.css') }}" rel="stylesheet">
    
    <link href="{{ url('/css/font-awesome.min.css') }}" rel="stylesheet">
    <link rel="stylesheet" href="{{ asset("assets/stylesheets/styles.css") }}" />
    <link href="{{ url('/css/dataTables.bootstrap.css') }}" rel="stylesheet">
    <!-- DataTables Responsive CSS -->
    <link href="{{ url('/datatables/dataTables.responsive.css')}}" rel="stylesheet">
    <link rel="stylesheet" href="{{ url('/css/bootstrap.min.css') }}">
    <link href="{{ url('/css/zunistyle.css') }}" rel="stylesheet">
    <script src="{{ url('/js/app.js') }}"></script>
    <script>
        window.Laravel=<?php echo json_encode(['csrfToken' => csrf_token()]); ?>
    </script>
</head>
<body>
    @if(Auth::user())
    <div id="wrapper" class="toggled">
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header" style="width: 250px;">
               
               <a style="font-weight: bold; padding-left: 10px;">
                <i id="menu-toggle" class="fa fa-bars fa-2x" data-toggle="collapse" data-target=".navbar-collapse" 
                style="margin-top: 10px; margin-left: 13px; float: left; display: inline-block;"></i>
                </a><p>
            </div>
            <ul class="nav navbar-top-links navbar-right">
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"> 
                        <image src="/images/avatar/avatar.png" class="nav-avatar"><span style="text-transform: capitalize; margin-left: 2px; margin-right: 2px;">{{Auth::user()->name}}</span> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="{{url('/profile')}}">
                            <span class="fa fa-user pull-right"></span>{{trans('common.information')}}</a></li>
                        <li class="divider"></li>
                        <li><a href="#"
                               onclick="event.preventDefault();
                                        document.getElementById('logout-form').submit();"><span
                                class="fa fa-sign-out pull-right"></span>{{trans('common.logout')}}
                            </a>
                            <form id="logout-form" action="{{ url('/logout') }}" method="POST" style="display: none;">
                                {{ csrf_field() }}
                            </form>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <div id="sidebar-wrapper">
                <div class="navbar-default sidebar" role="navigation">
                    <div class="sidebar-nav navbar-collapse">
                        <ul class="nav in" id="side-menu">
                            <li class="logo">
                                <img style="margin-top: 10px;  margin-bottom: 7px; max-width: 100%; height: auto;" src="{{ url('/images/logo/purpose.png') }}" />
                            </li>
                            <li class="@if(isset($active)) @if($active == 'dashboard') active @endif @endif">
                                <a href="{{ url('/dashboard') }}"><i class="fa fa-dashboard fa-fw"></i><span style="display: none;"> Dashboard</span></a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        <div id="page-wrapper">
            <div class="row">  
                @yield('section')
            </div>
            <!-- /#page-wrapper -->
        </div>
    </div>    
    <script src="{{ asset('assets/scripts/frontend.js') }}" type="text/javascript"></script>
        <!-- Scripts -->
    <script src="{{ url('/js/jquery.min.js') }}"></script>
    <script src="{{ url('/js/metisMenu.min.js') }}"></script>
    <script src="{{ url('/js/bootstrap.min.js') }}"></script>
    <!-- DataTabels -->
    <script src="{{ url('/datatables/jquery.dataTables.min.js') }}"></script>
    <script src="{{ url('/datatables/dataTables.bootstrap.min.js') }}"></script>
    <script src="{{ url('/datatables/dataTables.responsive.js') }}"></script>

    <script type="text/javascript">
    $(document).ready(function() {
        $("#menu-toggle").click(function(e) {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
            $("#wrapper.toggled").find("#sidebar-wrapper").find(".collapse").collapse("hide");
            if($(".sidebar li a span").css("display") != "none")    
                $(".sidebar li a span").css("display","none");
            else
                $(".sidebar li a span").css("display","");
        });
    });
    </script>
    @else
    <div class="" style="height: 100%">
    @endif @yield('login')</div>
    
</body>
</html>
