<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddAdminToUsersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        if(!DB::table('users')->where('username', '=', 'admin')->get()->isEmpty()) {
            return;
        }
        DB::table('users')->insert(
            array(
                'name' => 'Administrator',
                'email' => 'admin@unilever.com',
                'username' => 'admin',
                'phone' => '0965999334',
                'password' => bcrypt('admin@123')
            )
        );
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        DB::table('users')->where('username', '=', 'admin')->delete();
    }
}
