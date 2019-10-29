@extends('layouts.app')
@section('section')
<div class="row">
    <div class="col-sm-12">
        <div class="panel panel-primary" style="margin-top: 8px;">
            <div class="panel-heading">
                <span class="text-title">{{trans('common.survey_mange')}}</span>
                <div class="btn-group pull-right">
                	<form method="POST" action="{{url('/survey/export')}}" >
					{{ csrf_field() }}
						<input type="hidden" name="type" value="xlsx">
						<button type="submit" class="btn btn-success btn-sm"><i class="glyphicon glyphicon-export"></i></button>
					</form>
                </div>
            </div>
            <div class="panel-body">
                <table id="dataTables" width="100%" class="table table-striped">
                    <!-- Table Headings -->
	                <thead>
	                <tr>
	                    <th width="3%" class="text-center">#</th>
	                    <th width="20%">{{trans('common.email')}}</th>
						<th width="25%" class="text-center no-sort">{{trans('common.completed')}}</th>
	                    <th width="15%">{{trans('common.created_at')}}</th>
	                    <th width="4%" class="text-right no-sort">&nbsp;</th>
	                </tr>
	                </thead>
	                <!-- Table Body -->
                    <tbody>
                    @foreach ($surveys as $survey)
                    <tr>
                        <td hidden class="table-text">
	                        <div>{{ $survey->id }}</div>
	                    </td>

	                    <td class="table-text text-center">
	                        <div>{{ $loop->index + 1 }}</div>
	                    </td>

	                    <td class="table-text">
	                        <a href="mailto:{{$survey->email}}"><b>{{ $survey->email }}</b></a>
	                    </td>
						<td class="table-text text-center">
						<div class="progress" style="margin-bottom: 0 ! important;" >
							<div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" 
							aria-valuenow="{{$survey->completed}}" aria-valuemin="0" aria-valuemax="100" style="width:{{$survey->completed}}%">{{$survey->completed}}%</div>
						</div>
	                    </td>
                        <td class="table-text">
	                        <div>{{ Carbon\Carbon::createFromTimestamp($survey->updated_at)->format('d M Y') }}</div>
	                    </td>
                        <td class="table-text text-right">
		                    <a data-toggle="modal" data-target="#answerDetailModal" data-id="{{$survey->email}}" data-content="{{$survey->answers}}" class="btn btn-info btn-circle" href=""><i class="glyphicon glyphicon-stats" style="color: #ffffff;"></i></a>
		                </td>
                    </tr>
                    @endforeach
                    </tbody>
                </table>
            </div>
        </div>
    </div>
	@include('udsurvey')
    <script>
	    $(document).ready(function() {
	        $('#dataTables').DataTable({
	            responsive: true,
	            order: [[ 1, "asc" ]],
	            columnDefs:[{
	            	targets: 'no-sort',
	            	orderable: false
	            }],
	        });
    	});
    </script>
</div>
@stop
