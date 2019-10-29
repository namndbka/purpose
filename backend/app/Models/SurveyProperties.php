<?php
/**
 * Created by PhpStorm.
 * User: namnd
 * Date: 21/01/2018
 * Time: 10:52 AM
 */

namespace App\Models;

/**
 * @SWG\Definition(type="object", @SWG\Xml(name="SurveyProperties"))
 */
class SurveyProperties extends BaseModel {

    protected $fillable = ['title'];

    protected $casts = [
        'created_at' => 'timestamp',
        'updated_at' => 'timestamp'
    ];
}