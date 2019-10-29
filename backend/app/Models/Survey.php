<?php
/**
 * Created by PhpStorm.
 * User: namnd
 * Date: 4/10/2017
 * Time: 10:52 AM
 */

namespace App\Models;

/**
 * @SWG\Definition(type="object", @SWG\Xml(name="Survey"))
 */
class Survey extends BaseModel {

    protected $fillable = ['email'];

    protected $casts = [
        'created_at' => 'timestamp',
        'updated_at' => 'timestamp'
    ];
}