<jsp:include page="layout/header.jsp" />

<div class="header-panel">
	<h2 class="mt-3">Project Panel</h2>
	<button class="open-modal-btn new-task">New Task</button>
</div>

<div id="project-description"></div>

<div class="search-component">
	<h3>Search Users</h3>
	<div id="searchResults" class="user-grid"></div>
</div>
<div class="search-box">
	<button class="btn-search">
		<i class="fas fa-search"></i>
	</button>
	<input type="text" id="searchInput" class="input-search"
		placeholder="Type to Search...">
</div>

<h3 class="mt-5">Users in project</h3>
<div id="users-container" class="user-grid"></div>

<div id="tasks-container" class=""></div>

<!-- Modal -->
<div class="modal-overlay">
	<div class="modal-content">
		<div class="modal-header">
			<span class="close-btn text-black">&times;</span>
		</div>
		<div class="modal-body">
			<div class="form-container">
				<form action="#">
					<input type="hidden" id="taskId" name="taskId" value="">
					<!-- Title -->
					<div class="form-group">
						<label for="title" class="form-label">Title</label> <input
							type="text" id="title" name="title" maxlength="100"
							placeholder="Enter the task title" class="form-input" required>
					</div>

					<!-- Description -->
					<div class="form-group">
						<label for="description" class="form-label">Description</label>
						<textarea id="description" name="description"
							placeholder="Enter task description" class="form-input" required></textarea>
					</div>

					<!-- Due Date -->
					<div class="form-group">
						<label for="due_date" class="form-label">Due Date</label> <input
							type="date" id="due_date" name="due_date" class="form-input"
							required>
					</div>

					<!-- Priority -->
					<div class="form-group">
						<label for="priority" class="form-label">Priority</label> <select
							id="priority" name="priority" class="form-input" required>
							<option value="High">High</option>
							<option value="Medium">Medium</option>
							<option value="Low">Low</option>
						</select>
					</div>

					<!-- User Assigned -->
					<div class="form-group">
						<label for="user_assigned_id" class="form-label">User
							Assigned</label> <select id="user_assigned_id" name="user_assigned_id"
							class="form-input" required>

						</select>
					</div>

					<!-- Submit Button -->
					<button type="button" class="btn" id="createTask">Submit</button>
				</form>
			</div>
		</div>
	</div>
</div>


<script>
		$(document).ready(function() {
			
        	const email = getCookie('email');
            const userId = getCookie('userId');
            let tasks;
			
			var projectId = "${projectId}"
			var url = "${pageContext.request.contextPath}/tasks?projectId=" + encodeURIComponent(projectId);
			$.ajax({
			    url: url,  
			    type: 'GET',      
			    dataType: 'json', 
			    success: function(response) {
			    	console.log(response);
			    	const project = response.project;
			    	const autor = response.autor;
					tasks = response.tasks;
			        const users = response.users;
			        
			        if (parseInt(userId) === project.userId) {
			           
			            $('.open-modal-btn').show();
			            $('.search-component').show();
			            $('.search-box').show();
			            
			        } else {
			          
			            $('.open-modal-btn').hide();
			            $('.search-component').hide();
			            $('.search-box').hide();
			        }
			            
			            const projectHTML =
			            	'<h3>Project info</h3>' +
			                '<p>' + project.title + '</p>' +
			                '<p><strong>Autor:</strong> ' + autor + '</p>' +
			                '<p><strong>Descripción:</strong> ' + project.description + '</p>' +
			                '<p><strong>Fecha de inicio:</strong> ' + project.startDate + '</p>' +
			                '<p><strong>Fecha de finalización:</strong> ' + project.endDate + '</p>' +
			                '<p><strong>Estado:</strong> ' + (project.status ? 'Terminado' : 'No terminado') + '</p>' +
			                '<input type="hidden" value="' + project.userId + '" id="project-userId" />';
			            $('#project-description').html(projectHTML);
			            
				    	let deleteButtonHTML = '';
				        if (parseInt(userId) === project.userId) {
				            deleteButtonHTML = '<button class="btn-delete">Eliminar</button>';
				        }
			            
			            const tasksContainer = $("#tasks-container"); 

			            tasks.forEach(task => {
			                let priorityClass = task.priority.toLowerCase();
			                let statusClass = task.status ? "completed" : "";
			                let assignedUser = users.find(user => user.id === task.userAsignedId);
			                let assignedUserName = assignedUser ? assignedUser.name : "No asignado";

			                
			                let completeButtonHTML = '';
			                if (parseInt(userId) === task.userAsignedId && !task.status) {
			                   
			                    completeButtonHTML = '<button class="btn-complete" data-task-id="' + task.id + '">Marcar como Completa</button>';
			                }

			               
			                let deleteButtonHTML = '';
			                if (parseInt(userId) === project.userId) {
			                    deleteButtonHTML = '<button class="btn-delete" data-task-id="' + task.id + '">Eliminar</button>';
			                }
			                
			                let editButtonHTML = '';
			                if (parseInt(userId) === project.userId) {              
			                    editButtonHTML = '<button class="btn-edit" data-task-id="' + task.id + '">Edit</button>';
			                }


							let taskCardHTML = 
							    '<div class="task-card ' + priorityClass + ' ' + statusClass + '" id="task-' + task.id + '">' +  // id único
							        '<div class="ribbon ' + priorityClass + '">' + task.priority + '</div>' +
							        '<h2 class="task-title" id="task-title">' + task.title + '</h2>' +
							        '<p class="task-desc">' + task.description + '</p>' +
							        '<div class="task-details">' +
							            '<p><strong>Fecha límite:</strong> ' + task.dueDate + '</p>' +
							            '<p><strong>Prioridad:</strong> ' + task.priority + '</p>' +
							            '<p><strong>Asignado a:</strong> ' + assignedUserName + '</p>' +
							        '</div>' +
							        '<div class="task-actions">' +
							            completeButtonHTML +  
							            editButtonHTML +
							            deleteButtonHTML +    
							        '</div>' +
							    '</div>';


			                tasksContainer.append(taskCardHTML);
			            });
			        
		                const userSelect = $("#user_assigned_id");
		                users.forEach(user => {
		                    let userOption = '<option value="' + user.id + '">' + user.name + '</option>';
		                    userSelect.append(userOption);
		                });
		                
			            const usersContainer = $("#users-container");
			            users.forEach(user => {
			                let userCardHTML = 
			                    '<div class="user-card">' +
			                        '<div class="user-card-header">User Information</div>' +
			                        '<div class="user-card-body">' +
			                            '<h5 class="card-title">' + user.name + '</h5>' +
			                            '<p class="card-text">' + user.email + '</p>';

			                if (parseInt(userId) === project.userId) {
			                    userCardHTML += 
			                        '<div class="user-card-buttons">' +
			                            '<button class="btn btn-remove" data-user-id="' + user.id + '">Remove from Project</button>' +
			                        '</div>';
			                }

			                userCardHTML += 
			                        '</div>' +
			                    '</div>';

			                usersContainer.append(userCardHTML);
			            });
			     
			    },
			    error: function(xhr, status, error) {
			    	console.log(status);
			        console.error("Error al obtener el proyecto:", error);
			    }
			});
			
			// complete a task
			$('#tasks-container').on('click', '.btn-complete', function(event) {
				const taskId = $(this).data('task-id');
				console.log("Marcar como completa la tarea con ID:", taskId);
		            
				if (!confirm("¿Estás seguro de que deseas completar esta tarea?")) {
		        	return;
		        }
				
				$.ajax({
					url: '${pageContext.request.contextPath}/tasks-actions',       
					type: 'PUT',           
					contentType: 'application/json', 
					dataType: 'json',       
					data: JSON.stringify({  
						taskId: taskId,
	        	       	userId: parseInt(userId)
	        	    }),
					success: function(response) {
						alert("Task terminada.");
		                location.reload();
					},
					error: function(xhr, status, error) {
		            	alert("Hubo un error al intentar eliminar la tarea. Inténtalo de nuevo.");
		                console.error("Error en la eliminación:", error);
		            }
		        });
		    });
			 
			// delete a task
		    $('#tasks-container').on('click', '.btn-delete', function(event) {
		    	const taskId = $(this).data('task-id');
		        console.log("Eliminar tarea con ID:", taskId);
		            
		   	    if (!confirm("¿Estás seguro de que deseas eliminar esta tarea?")) {
		        	return;
		        }
		            
				$.ajax({
		        	url: '${pageContext.request.contextPath}/tasks',       
	        	    type: 'DELETE',           
	        	    contentType: 'application/json', 
	        	    dataType: 'json',       
	        	    data: JSON.stringify({  
	        	    	projectId: projectId,
	        	       	userProjectId: userId,
	        	       	taskToUpdateId: taskId
	        	    }),
		            success: function(response) {
		            	alert("Tarea eliminada correctamente.");
		                $('#task-' + taskId).remove();		                    
					},
					error: function(xhr, status, error) {
		            	alert("Hubo un error al intentar eliminar la tarea. Inténtalo de nuevo.");
		                console.error("Error en la eliminación:", error);
		            }
		        });
		     });
			
		    // update task    
			$('#tasks-container').on('click', '.btn-edit', function(event) {
				event.preventDefault();
				const taskId = $(this).data('task-id'); 
				console.log("Editando la tarea con ID:", taskId);
		        const task = tasks.find(t => t.id === taskId);
		        if (task) {
					console.log("task asignada ->", taskId);
		            $('#taskId').val(task.id);  
		            console.log("taskId después de llenarlo ->", $('#taskId').val());  
		            $('#title').val(task.title);
		            $('#description').val(task.description);
		            $('#due_date').val(task.dueDate);
		            $('#priority').val(task.priority);
		            $('#user_assigned_id').val(task.userAsignedId);
		            $('.modal-overlay').fadeIn();
		        } else {
		            console.error("Tarea no encontrada.");
		        }
		    });

			// create or update
			$('#createTask').click(function(event){
				event.preventDefault();
		     
				var taskId = $('#taskId').val();  
				console.log("taskId recuperado ->", taskId);
				var title = $('#title').val();
				var description = $('#description').val();
				var dueDate = $('#due_date').val();
				var priority = $('#priority').val();
				var userAsigned = $('#user_assigned_id').val();
				var userProjectId = $('#project-userId').val();
				
				let data = {};
		        data.title = title;
				data.description = description;
				data.dueDate = dueDate;
				data.priority = priority;
				data.userAsignedId = parseInt(userAsigned);
				data.projectId = parseInt(projectId);  
				data.userProjectId = parseInt(userProjectId);
				
				var method = taskId ? 'PUT' : 'POST';  
					
				if (taskId) {
					console.log("Entra a editar, método:", method);
					data.taskToUpdateId = parseInt(taskId);  
					console.log("Datos para editar:", data);
				} else {
					console.log("Entra a nuevo, método:", method);
					console.log("Datos para crear:", data);
		        }

				$.ajax({
					url: '${pageContext.request.contextPath}/tasks',  
					type: method,  
					contentType: 'application/json',
					dataType: 'json',
					data: JSON.stringify(data),
					success: function(response) {
						console.log("Tarea creada/actualizada con éxito:", response);
						setTimeout(function() {
							window.scrollTo(0, 0);
						}, 100);
						location.reload();
					},
					error: function(xhr, status, error) {
			            console.error("Error al crear/actualizar la tarea:", error);
						var errors = JSON.parse(xhr.responseText);
						console.error("Errores recibidos:", errors);
						$('.error-message').remove();  
						for (var field in errors) {
							var errorMessage = errors[field];
							var inputId = field;
							if (field === "dueDate") inputId = "due_date";
							if (field === "endDate") inputId = "end_date";
							$('#' + inputId).after('<span class="error-message" style="color: red;">' + errorMessage + '</span>');
			            }
			        }
		      	});
		    });

            // serach users to asigned to project
            $('#searchInput').on('input', function() {
                const keyword = $(this).val().trim();
                
                if (keyword === "") {
                    $('#searchResults').html("");
                    return;
                }
                $.ajax({
                    url: '${pageContext.request.contextPath}/search-user',
                    type: 'POST',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: JSON.stringify({ 
                    	keyword: keyword,
                    	userId: parseInt(userId),
                    	projectId: parseInt(projectId)
                    	}),
                    success: function(response) {
                    	console.log("Respuesta JSON del servidor:", response);
                        $('#searchResults').html("");  
                        if (response.message) {
                            $('#searchResults').html("<p>" + response.message + "</p>");
                        } else {
                            const users = response;
                            users.forEach(user => {
                                let userCardHTML = 
                                    '<div class="user-card-2">' +
                                        '<div class="user-card-header">User Information</div>' +
                                        '<div class="user-card-body">' +
                                            '<h5 class="card-title">' + user.name + '</h5>' +
                                            '<p class="card-text">' + user.email + '</p>' +

                                           
                                            '<div class="user-card-buttons">' +
                                                '<button class="btn btn-add" data-user-id="' + user.id + '">Add to Project</button>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>';
                                $('#searchResults').append(userCardHTML);
                            });
                        }
                    },
                    error: function(xhr, status, error) {
                        console.log("Error al realizar la búsqueda:", error, status);
                        $('#searchResults').html("<p>Error al realizar la búsqueda.</p>");
                    }
                });
            });
            
            // add user to project
            $(document).on('click', '.btn-add', function() {
                const userId = $(this).data('user-id'); 
                console.log('Agregar al proyecto el usuario con id:', userId);
                $('#searchInput').val('');
                $('#searchResults').empty();

                $.ajax({
                    url: '${pageContext.request.contextPath}/tasks-actions', 
                    type: 'POST', 
                    contentType: 'application/json', 
                    data: JSON.stringify({
                    	userId: userId,
                    	projectId: parseInt(projectId)
                    }), 
                    success: function(response) {
                        console.log('Usuario agregado al proyecto:', response);
                        alert("Usuario agregado correctamente.");
                        location.reload();
                        $('#message').text('Usuario asignado al proyecto exitosamente').addClass('success').removeClass('error');
                    },
                    error: function(xhr, status, error) {
                        console.error('Error al agregar usuario:', error);
                        $('#message').text('Hubo un error al asignar el usuario al proyecto').addClass('error').removeClass('success');
                    }
                });
                
            });
            
            // remove user form project
            $(document).on('click', '.btn-remove', function() {
                const userId = $(this).data('user-id'); 
                console.log('remove al proyecto el usuario con id:', userId);
				
				if (!confirm("¿Estás seguro de que deseas quitar este usuario?")) {
					return;
		        }

                $.ajax({
                    url: '${pageContext.request.contextPath}/tasks-actions', 
                    type: 'DELETE', 
                    contentType: 'application/json', 
                    data: JSON.stringify({
                    	userId: userId,
                    	projectId: parseInt(projectId)
                    }), 
                    success: function(response) {
                        console.log('Usuario agregado al proyecto:', response);
                        alert("Usuario removido correctamente.");
                        location.reload();
                        $('#message').text('Usuario asignado al proyecto exitosamente').addClass('success').removeClass('error');
                    },
                    error: function(xhr, status, error) {
                        console.error('Error al agregar usuario:', error);
                        $('#message').text('Hubo un error al asignar el usuario al proyecto').addClass('error').removeClass('success');
                    }
                });
                
            });
            
		    // modal
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
            	//$('#taskId').val('');
            	$('.error-message').remove();
            	//$('#taskId').val('');
                if (e.target === this) { 
                    $(this).fadeOut();
                }
            });

			// clean form modal
			$('.new-task').on('click', function() {
				$('#taskId').val('');
				$('#title').val('');
		        $('#description').val('');
		        $('#due_date').val('');
		        $('#priority').val('');	
		        $('#user_assigned_id').val('');
	          	$('.error-message').remove();
	            $('.modal-overlay').fadeIn();
	        });
		});
	</script>


<jsp:include page="layout/footer.jsp" />