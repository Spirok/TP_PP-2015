
var id_usr_elejido = -1;

function init() {
	$("#refrescar_empresa").hide();
	var id_perfil = obtenerIdPerfil();

	// usuario tipo operador
	if (id_perfil == 2) {
		$("#contenedor_form_combo").hide();
		$("#campo_responsable").hide();
		$("#campo_email_resp").hide();
		$("#campo_consultas_a").hide();
		$("#campo_usuario").hide();
		$("#campo_passwd").hide();
		$("#btnEliminar").hide();
		$("#btnRegistrar").hide();
		$("#estado").prop("disabled", true);
		$("#ayuda_empresa").show();
		$("#area_form").attr('class', 'col-md-7');
		$("#refrescar_empresa").show();

		var cod_e = obtenerCodEmpresaOperador(); // de utilidades.js

		if (cod_e == 0) {
			$("#conjunto_botones").hide();
			$("#form_empresa :input").attr("disabled", true);
			$('#contenido-modal').text('No tiene asigando una empresa');
			$('#myModal').modal('show');
		} else {
			obtener_empresa(cod_e);
			$("#refrescar_empresa").click(function(event) {
				obtener_empresa(cod_e);
			});
		}
	}

	// usuario tipo administrador
	if (id_perfil == 1) {
		// de utilidades.js, cargo todas las empresas en el combobox
		listado_empresas();

		// cargo lista de usuarios empresa
		listado_usuarios_empresa();

		// si cambia de opcion en el combobox muestro datos de empresa seleccionada
		$("#select_empresa").change(function() {
				limpiarErrores();
				var codigo_e = $(this).val();
				obtener_empresa(codigo_e);

		});

		$("#listaUsuarioEmpresa").on( "click", "li", function() {
			var str = $(this).text().split(' ');
			// seteo usuario y pass
			$("#usuario").val(str[0]);
			$("#clave").val(str[1]);
			//alert(this.id);
			id_usr_elejido = this.id;
			revalidate();
		});
	}

	$("#btnLimpiar").click(function(event) {
		document.getElementById("select_empresa").selectedIndex =  -1;
		limpiarErrores();
	});

	// validacion del form
	validacionForm();
}

// ----------------------------------------------------------------------------------------------

/*
 funcion que realiza el abm de empresas. Se le indica accion : 'alta', 'baja' o 'modificacion'.
 Si no se producen errores se muestra en mensaje en ventana modal.
*/
function realizarAbm(accion) {
	iniciarBlockUI(" Procesando...");
	$.ajax({
		type     : "post",
		dataType : "json",
		url      : accion,
		data     : $("#form_empresa").serialize() + "&id_usr=" + id_usr_elejido + "&estado=" + $("#estado").is(':checked'),// datos del formulario + accion
		success  : function(resultado) {
			
			if (resultado["resultado"] == "ok") {
				listado_empresas();
				listado_usuarios_empresa();
				$("#btnLimpiar").click();
			} 
			//if (resultado["resultado"] == "error") {
				//
			//}
			// seteo contenido de la ventana modal
			$('#contenido-modal').text(resultado["mensaje"]);
			$('#myModal').modal('show');
		},
		complete: function() { 
			terminarBlockUI();
		},
		error: function(xhr, status, error) {
			terminarBlockUI();
			$('#contenido-modal').text('Error, no se pudo establecer la conexion.' + error);
			$('#myModal').modal('show');
		}
	});
}



// ----------------------------------------------------------------------------------------------

/*
funcion que se encarga de validar en fomulario de empresas. Si no hay errores de validacion
se procede a ejecutar la accion (abm)
*/
function validacionForm() {
	$('#form_empresa')
		.on('success.form.fv', function(e) {
			// si no hay errores ejecuto la accion segun el boton clickeado
 			var $form        = $(e.target),     // Form instance
                // Get the clicked button
                $button      = $form.data('formValidation').getSubmitButton();

			// Prevent form submission
	        e.preventDefault();
	        if ($button == null) return;
	        // chequeo que boton fue clickeado
            switch ($button.attr('id')) {
                case 'btnRegistrar':
                    //alert('btnRegistrar');
                    realizarAbm("/agregar_empresa");
                    break;
                case 'btnModificar':
                    //alert('btnModificar');
                    realizarAbm('/modificar_empresa');
                    break;

                case 'btnEliminar':
                    //alert('btnEliminar');
                    realizarAbm('/eliminar_empresa');
                    break;
            }
		})
		.formValidation({
			// solo numeros enteros positivos                                   : /^\d*$/
			// permite letras minusculas mayusculas . _ - ( )                   : /^[a-z-A-Z-0-9\.\_\-\(\)]/
			// solo letras y numeros                                            : /^[a-zA-Z0-9]*$/
			// email solo formato x@x.x                                         : /^[a-z-A-Z-0-9\.\_\-]+@+[a-z]+.+[a-z-A-Z]*$/
			// url permite letras minusculas mayusculas . _ - ( ) : / ? & %     : /^[a-z-A-Z-0-9\.\_\-\(\)\:\/\?\&\%]*$/
			framework: 'bootstrap',
			excluded: false,
			err: {
				container: 'tooltip'
			},

			icon: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				/*
				cod_empresa: {
					validators: {
						notEmpty: {
							message: 'El campo código es requerido y no puede estar vacío'
						},
						regexp: {
							regexp: /^\d*$/,
							message: 'El campo código sólo puede tener valores enteros'
						},
						stringLength: {
							min: 1,
							max: 11,
							message: 'El campo código debe tener entre 1 y 11 caracteres'
						}
					}
				},
				*/
				nombre: {
					validators: {
						notEmpty: {
							message: 'El campo nombre es requerido y no puede estar vacío'
						},
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 100,
							message: 'El campo nombre no puede tener mas de 100 caracteres'
						}
					}
				},
				direccion : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 100,
							message: 'El campo direccion no puede tener mas de 100 caracteres'
						}
					}
				},
				cod_postal : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 15,
							message: 'El campo codigo postal no puede tener mas de 15 caracteres'
						}
					}
				},
				localidad : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo localidad no puede tener mas de 80 caracteres'
						}
					}
				},
				provincia : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo provincia no puede tener mas de 80 caracteres'
						}
					}
				},
				telefono : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo telefono no puede tener mas de 80 caracteres'
						}
					}
				},
				fax : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo fax no puede tener mas de 80 caracteres'
						}
					}
				},
				email : {
					validators: {
						emailAddress: {
					 		message: 'El correo electronico no es valido. El formato debe ser y@y.y'
				 		},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo email no puede tener mas de 80 caracteres'
						}
					}
				},
				web : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\.\_\-\(\)\:\/\?\&\%\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo web no puede tener mas de 80 caracteres'
						}
					}
				},
				responsable : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo responsable no puede tener mas de 80 caracteres'
						}
					}
				},
				email_resp : {
					validators: {
						emailAddress: {
					 		message: 'El correo electronico no es valido. El formato debe ser y@y.y'
				 		},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo email responsable no puede tener mas de 80 caracteres'
						}
					}
				},
				consultas_a : {
					validators: {
						regexp: {
							regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
						},
						stringLength: {
							min: 0,
							max: 80,
							message: 'El campo consultas a no puede tener mas de 80 caracteres'
						}
					}
				},
				usuario: {
					validators: {
						notEmpty: {
							message: 'El campo usuario es requerido y no puede estar vacío'
						},
						regexp: {
							regexp: /^[a-zA-Z0-9\ñ\Ñ]*$/,
							message: 'El campo usuario sólo puede tener letras y numeros'
						},
						stringLength: {
							min: 1,
							max: 11,
							message: 'El campo usuario debe tener entre 1 y 11 caracteres'
						}
					}
				},
				clave: {
					validators: {
						notEmpty: {
							message: 'El campo clave es requerido y no puede estar vacío'
						},
						regexp: {
							regexp: /^[a-zA-Z0-9\ñ\Ñ]*$/,
							message: 'El campo clave sólo puede tener letras y numeros'
						},
						stringLength: {
							min: 1,
							max: 11,
							message: 'El campo clave debe tener entre 1 y 11 caracteres'
						}
					}
				}
			

			}
		});
}

// ----------------------------------------------------------------------------------------------

/*
 funcion que toma los campos del formulario de empresas y limpia los iconos/mensajes
 de errores.
*/
function limpiarErrores() {
	var fields = $('#form_empresa').data('formValidation').getOptions().fields, $parent, $icon;
	for (var field in fields) {
		$parent = $('[name="' + field + '"]').parents('.form-group');
		$icon   = $parent.find('.form-control-feedback[data-fv-icon-for="' + field + '"]');
		$icon.tooltip('destroy');
	}
	// Then reset the form
	$('#form_empresa').data('formValidation').resetForm(true);

}


// ----------------------------------------------------------------------------------------------

/*
funcion que le envia por post al uri /info_empresas los datos
'select_empresa' : codigo_empresa
 retorna los datos de la empresa seleccionada en formato json.
 Si no hay error se cargan los datos de la empresa en el formulario.
*/
function obtener_empresa(emp) {
	var datos =  { select_empresa : emp };
	iniciarBlockUI(" Buscando Empresa...");
	$.ajax({
		url      : '/info_empresa',
		type     : 'post',
		dataType : 'json',
		data     : datos,
		success  : function(respuesta) {
			console.log("obtener_empresa() " + respuesta);
			if (respuesta["resultado"] == "error") {
				$("#btnLimpiar").click();
				return;
			}
			console.log(respuesta);
			$("#cod_empresa").val(respuesta.codigo);
			$("#nombre").val(respuesta.nombre);
			$("#direccion").val(respuesta.direccion);
			$("#cod_postal").val(respuesta.cod_postal);
			$("#localidad").val(respuesta.localidad);
			$("#provincia").val(respuesta.provincia);
			$("#telefono").val(respuesta.telefono);
			$("#fax").val(respuesta.fax);
			$("#email").val(respuesta.email);
			$("#web").val(respuesta.web);
			$("#responsable").val(respuesta.responsable);
			$("#email_resp").val(respuesta.email_responsable);
			$("#consultas_a").val(respuesta.consultas_a);
			$("#usuario").val(respuesta.nick);
			$("#clave").val(respuesta.password);
			var est = ((respuesta.estado == 'false') ? false : true);
			$("#estado").prop( "checked", est);
			id_usr_elejido = respuesta.id_usr;
		},
		complete: function() { 
			terminarBlockUI();
			$('#form_empresa').data('formValidation').validate();
		},
		error: function(xhr, status, error) {
			terminarBlockUI();
			$('#contenido-modal').text('Error, no se pudo establecer la conexion.');
			$('#myModal').modal('show');
		}
	})
}


/*
funcion que realiza una peticion post a uri /operadores_disponibles la cual devuelve datos de 
los usuarios operadores(usuarios de empresa) en formato json.
Se recorren los usuarios y se cargan en una lista ul.
*/
function listado_usuarios_empresa() {
	iniciarBlockUI(" Cargando...");
	$.ajax({
		url      : '/operadores_disponibles',
		type     : 'post',
		dataType : 'json',
		success  : function(result) {
			var lista = "";
			// borro el contenido de la lista
			$('#listaUsuarioEmpresa').empty();
			// obtengo los usuarios retornados por php en formato json
			var usuarios = result['Records'];
			// recorro los usuarios
			for (var i = 0; i < usuarios.length; i++) {
				lista += '<li id='+ usuarios[i].cod_usuario +'><a>' + usuarios[i].nick + ' ' + usuarios[i].password +'</a></li>';
			}
			// agrego los usuarios actuales a la lista
			$('#listaUsuarioEmpresa').append(lista);
		},
		complete: function() { 
			terminarBlockUI();
		},
		error: function(xhr, status, error) {
			terminarBlockUI();
			$('#contenido-modal').text('Error, no se pudo establecer la conexion.');
			$('#myModal').modal('show');
		}
	})
}


function revalidate() {
    $('#form_empresa')
        //.formValidation('revalidateField', 'codigo')
        .formValidation('revalidateField', 'usuario')
        .formValidation('revalidateField', 'clave');
}
