<div id="answerDetailModal" class="modal fade" role="dialog" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
        	<div class="modal-header" style="padding: 5px;">
        		<h4 class="model-title text-center" style="text-transform: uppercase;">
        			<i class="fa fa-envelope"></i><input type="text" id="title" disabled></input></h4>
        	</div>
        	<form class="form-horizontal" roler="form" id="answerViewForm">
        		<div class="modal-body" style="font-size: 13px">
        			<table class="table">
        				<thead>
        					<tr>
        						<th width="3%" class="table-text text-center">#</th>
	                    		<th></th>
        					</tr>
        				</thead>
        				<tbody>
        					
        				</tbody>
        			</table>
        		</div>
        		<div class="modal-footer">
			      <button type="button" id="collapsible" class="btn btn-default">Close</button>
			    </div>
        	</form>
        </div>
    </div>
</div>
<script type="text/javascript">
	$(function (){
		$('#answerDetailModal').on('show.bs.modal', function (survey) {
			var id = $(survey.relatedTarget).data('id');
			$(this).find('.modal-header').find('#title').val(id);
			var answers = $(survey.relatedTarget).data('content');
			if(answers.constructor === Array) {
				var $view = $(this).find('.table').find('tbody');
				$view.children().remove()
				var answersLength = answers.length;
				for (var i = 0; i < answersLength; i++) {
					var completed = answers[i]["completed"];
				    $view.append('<tr><td class="table-text text-center"><b>'+ answers[i]["sid"] +'</b></td><td class="table-text text-center"><div class="progress" style="margin-bottom: 0 ! important;" ><div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="'+completed+'" aria-valuemin="0" aria-valuemax="100" style="width: '+ (completed) +'%">'+(completed)+'%</div></div></td></tr>');
				}
			}
			
		});
	})
</script>