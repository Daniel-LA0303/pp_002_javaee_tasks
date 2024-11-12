<jsp:include page="layout/header.jsp"/>

	<div class="header-panel">
		<h3 class="mt-3">My projects</h3>
		<!-- modal section -->
		<button class="open-modal-btn new-project">New Project</button>    
	</div>


	<!-- Cards section -->
	<div class="ag-format-container">
		<!-- show cards projects -->
		<div class="ag-courses_box" id="projects-container">
	  	</div>
	</div>
	
	<h3 class="mt-3">Projects I am assigned to</h3>
	
	<!-- Cards section -->
	<div class="ag-format-container">
		<!-- show cards projects -->
		<div class="ag-courses_box" id="projects-container-to-asigned">
	  	</div>
	</div>

	<!-- Modal -->
 	<div class="modal-overlay">
		<div class="modal-content">
			<div class="modal-header">
				<span class="close-btn text-black">&times;</span>
			</div>
			<div class="modal-body">	
				<div class="form-container ">
					<form action="#" >
						<input type="hidden" id="projectId" name="taskId" value="">
						<div class="form-group">
							<label for="title" class="form-label">Title</label>
							<input type="text" id="title" name="title" maxlength="100" placeholder="Enter the event title" class="form-input" required>
						</div>
		
						<div class="form-group">
							<label for="description" class="form-label">Description</label>
							<textarea id="description" name="description" placeholder="Enter event description" class="form-input" required></textarea>
		                </div>
		
		                <div class="form-group">
		    	            <label for="start_date" class="form-label">Start Date</label>
		                    <input type="date" id="start_date" name="start_date" class="form-input" required>
		                </div>
		
		                <div class="form-group">
		                	<label for="end_date" class="form-label">End Date</label>
		                    <input type="date" id="end_date" name="end_date" class="form-input" required>
		           	    </div>
		
		                <button type="button" class="btn" id="newProject">Submit</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	
    <script type="text/javascript">
        $(document).ready(function() {
        	
            const email = getCookie('email');
            const userId = getCookie('userId');
            let projects;
        	
        	//get personal projetcs and projects assigned
		    var url = "${pageContext.request.contextPath}/projects?email=" + encodeURIComponent(email);
		    $.ajax({
		        url: url,
		        type: 'GET',
		        success: function(response) {
					console.log(response);  
		        	projects = response.projects;
		        	 
		        	var projectsAsigned = response.projectsAsigned;
		        	let cards = '';
		            projects.forEach(project => {
		            	var title = project.title;
		                var description = project.description;
		                var startDate = project.startDate;
		       	        var projectId = project.id;
		                   
		                cards += '<div class="ag-courses_item">' +
		                    '<div class="ag-courses-item_link">' +
		                        '<a href="${pageContext.request.contextPath}/project?id=' + projectId + '" class="">' +
		                            '<div class="ag-courses-item_bg"></div>' +
		                            '<div class="ag-courses-item_title">' +
		                                title +
		                            '</div>' +
		                            '<div class="ag-courses-item_date-box">' +
		                                'Start: ' +
		                                '<span class="ag-courses-item_date">' +
		                                    startDate +
		                                '</span>' +
		                            '</div>' +
		                        '</a>' +
		                        '<div class="ag-courses-item_actions">' +
		                            '<button class="edit-project-btn" data-project-id="' + projectId + '">Editar</button>' +
		                            '<button class="delete-project-btn" data-project-id="' + projectId + '">Eliminar</button>' +
		                        '</div>' +
		                    '</div>' +
		                '</div>';

		            });
		          	$('#projects-container').html(cards);
					
		          	cards = '';
					
					projectsAsigned.forEach(project => {
		            	var title = project.title;
		                var description = project.description;
		                var startDate = project.startDate;
		                var projectId = project.id;
		                   
		                cards += '<div class="ag-courses_item">' +
		                    			'<a href="${pageContext.request.contextPath}/project?id=' + projectId + '" class="ag-courses-item_link">' +
		                                    '<div class="ag-courses-item_bg"></div>' +
		                                    '<div class="ag-courses-item_title">' +
		                                        title +
		                                    '</div>' +
		                                    '<div class="ag-courses-item_date-box">' +
		                                        'Start: ' +
		                                        '<span class="ag-courses-item_date">' +
		                                            startDate +
		                                        '</span>' +
		                                    '</div>' +
		                                    '<div class="ag-courses-item_description">' +
		                                        description +
		                                    '</div>' +
		                                '</a>' +
		                            '</div>';
		                });
		                $('#projects-container-to-asigned').html(cards);
		        },
		        error: function(xhr, status, error) {
		            console.error("Error en la solicitud:", error);
		        }
		    });
		    
		    // delete a project
		    $('#projects-container').on('click', '.delete-project-btn', function() {
		        const projectId = $(this).data('project-id');
		        console.log("proyecto a eliminar: ", projectId);
		        
	            if (!confirm("¿Estás seguro de que deseas eliminar esta tarea?")) {
	                return;
	            }
	            $.ajax({
	            	url: '${pageContext.request.contextPath}/projects',       
        	        type: 'DELETE',           
        	        contentType: 'application/json', 
        	        dataType: 'json',       
        	        data: JSON.stringify({  
        	        	 projectId: projectId,
        	        	 userId: parseInt(userId),
        	        }),
	                success: function(response) {
	                    alert("Proyecto eliminada correctamente.");

	                    location.reload();
					},
	                error: function(xhr, status, error) {
	                    alert("Hubo un error al intentar eliminar la tarea. Inténtalo de nuevo.");
	                    console.error("Error en la eliminación:", error);
	                }
	            });
		    });
            
            // edit a project
		    $('#projects-container').on('click', '.edit-project-btn', function() {
		        const projectId = $(this).data('project-id');
		        console.log("proyecto a editar: ", projectId);
		        
	           	const project = projects.find(p => p.id === projectId);
	           
	           	$('#projectId').val(project.id);  
	       		$('#title').val(project.title);
	    		$('#description').val(project.description);
	    		$('#start_date').val(project.startDate);
	    		$('#end_date').val(project.endDate);
	    		$('.modal-overlay').fadeIn();
	           console.log(project);
		    });
            
            // post a new project or update a project
        	$('#newProject').click(function(event){
        		event.preventDefault();
        		
        		var projectId = $('#projectId').val();  
        		var title = $('#title').val();
        		var description = $('#description').val();
        		var dateStart = $('#start_date').val();
        		var dateEnd = $('#end_date').val();
        		
        		console.log(dateStart, dateEnd, title, description, projectId);
        		
        		let data = {};
        		data.title = title;
	            data.description = description;
	            data.startDate = dateStart;
	            data.endDate = dateEnd;
	            data.userId = parseInt(userId);
	            
	            var method = projectId ? 'PUT' : 'POST';
	            
	            if (projectId) {
	                console.log("Entra a editar, método:", method);
	                data.projectId = parseInt(projectId);  
	                console.log("Datos para editar:", data);
	            } else {
	                console.log("Entra a nuevo, método:", method);
	                console.log("Datos para crear:", data);
	            }
        		
        		$.ajax({
        	        url: '${pageContext.request.contextPath}/projects',       
        	        type: method,           
        	        contentType: 'application/json', 
        	        dataType: 'json',       
        	        data: JSON.stringify(data),
        	        success: function(response) {
        	            console.log("Proyecto creado con éxito:", response);
        	            setTimeout(function() {
        	                window.scrollTo(0, 0);
        	            }, 100);
        	            location.reload();
        	            window.scrollTo(0, 0);
        	        },
        	        error: function(xhr, status, error) {
        	            console.error("Error al crear el proyecto:", error);
        	            
        	            console.error("Error al crear el proyecto:", error);
        	            var errors = JSON.parse(xhr.responseText);
        	            console.error("Errores recibidos:", errors);
 
        	            $('.error-message').remove();
        	            for (var field in errors) {
			                var errorMessage = errors[field];
			                
			                
			                var inputId = field;
			                if (field === "startDate") inputId = "start_date";
			                if (field === "endDate") inputId = "end_date";
			                
			                
			                $('#' + inputId).after('<span class="error-message" style="color: red;">' + errorMessage + '</span>');
			            }
        	        }
        	    });        		
        	});
            
            // modal section
            // open modal
            $('.open-modal-btn').on('click', function() {
            	$('.error-message').remove();
                $('.modal-overlay').fadeIn();
            });

            // close modal
            $('.close-btn').on('click', function() {
            	$('.error-message').remove();
                $('.modal-overlay').fadeOut();
            });

            // close modal dinamic
            $('.modal-overlay').on('click', function(e) {
            	$('.error-message').remove();
                if (e.target === this) { 
                    $(this).fadeOut();
                }
            });
            
            // clean modal form
            $('.new-project').on('click', function() {
            	$('#projectId').val('');  
	       		$('#title').val('');
	    		$('#description').val('');
	    		$('#start_date').val('');
	    		$('#end_date').val('');
            	$('.error-message').remove();
                $('.modal-overlay').fadeIn();
            });
        });
    </script>
<jsp:include page="layout/footer.jsp"/>