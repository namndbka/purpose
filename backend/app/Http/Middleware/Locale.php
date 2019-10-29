<?php

namespace App\Http\Middleware;

use Closure;
use Auth;
use Config;

class Locale
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $user = Auth::user();
        $currtLang = "vi";
        if($user) {
            $currLang = Auth::user()->lang;
        }
        app()->setLocale($currtLang);
        return $next($request);
    }
}
