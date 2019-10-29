<?php

namespace App\Http\Middleware;

use \Illuminate\Auth\Middleware\Authenticate;
use Closure;
use Auth;
use Config;
use Response;

class AccessApi extends Authenticate
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next,  ...$guards)
    {

        if(!isset($_SERVER['HTTP_FROM'])){  
            return Response::json(['error'=>'please_set_client_header']);  
        }  
  
        if($_SERVER['HTTP_FROM'] != 'androi_survey' && $_SERVER['HTTP_FROM'] != 'ios_survey'){  
            return Response::json(['error'=>'wrong_client_header']);  
        }
        // $this->authenticate($guards);
        return $next($request);
    }

}
