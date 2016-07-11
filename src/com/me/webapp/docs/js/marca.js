

function init() {
	listado_marcas();

		// si cambia de opcion en el combobox muestro datos de la marca seleccionada
	$("#select_marca").change(function() {
		obtener_marca();
	});


    $("#btnLimpiar").click(function() {
        document.getElementById("select_marca").selectedIndex =  -1;
        
        var fields = $('#form_marca').data('formValidation').getOptions().fields,
            $parent, $icon;

        for (var field in fields) {
            $parent = $('[name="' + field + '"]').parents('.form-group');
            $icon   = $parent.find('.form-control-feedback[data-fv-icon-for="' + field + '"]');
            $icon.tooltip('destroy');
        }
        // Then reset the form
        $('#form_marca').data('formValidation').resetForm(true);

        //document.getElementById("select_perfil").selectedIndex =  -1;
    });

	validator();
}

/*
 funcion que realiza una peticion post al sevlet /listado_empresas el cual devuelve un objeto json con todos los
 usuarios existentes. Estos usuarios se rellenan en un combobox.
*/
function listado_marcas() {
    iniciarBlockUI(" Cargando...");
    $.ajax({
        url: '/listado_marcas',
        type: 'post',
        dataType: 'json',
        success : function(resultado) {
            // obtengo combobox
            combobox = document.getElementById("select_marca");

            // seteo el largo del bombobox a 0
            combobox.options.length = 0;

            // obtengo todos los rubros
            var rubros = resultado.Records;
            for (var i = 0 ; i < rubros.length ; i++) {
                //console.log(empresas[i].nick);        
                var opcion   = document.createElement("option");
                opcion.value = rubros[i].codigo;
                opcion.text  = rubros[i].nombre;
                combobox.add(opcion);
            }
            // ninguna opcion seleccionada
            combobox.selectedIndex = -1;
        },
        complete: function() {
            terminarBlockUI();
        },
        error: function(xhr, status, error) {
            terminarBlockUI();
            //$('#contenido-modal').text('Error, no se pudo establecer la conexion.');
            //$('#myModal').modal('show');
        }
    })
}



/*
funcion que le envia por post al uri /info_rubros los datos
'accion' : 'obtener_empresa'
'empresa_seleccionada' : empresa seleccionada del combobox
 php los procesa y retorna los datos de la empresa seleccionada en formato json.
 Si no hay error se cargan los datos de la empresa en el formulario.
*/
function obtener_marca() {
	//alert($("#select_empresa option:selected").text() );
	var marca = $("#select_marca").val(); // option:selected
	var datos =  { marca_seleccionado : marca };
	iniciarBlockUI(" Buscando Marca...");
	$.ajax({
		url      : '/info_marca',
		type     : 'post',
		dataType : 'json',
		data     : datos,
		success  : function(respuesta) {
			if (respuesta["resultado"] == "error") {
				$("#btnLimpiar").click();
				return;
			}
			console.log(respuesta);
			$("#codigo").val(respuesta.codigo);
			$("#nombre").val(respuesta.nombre);

			var est = ((respuesta.estado == 'false') ? false : true);
			$("#estado").prop( "checked", est);

		},
		complete: function() { 
			terminarBlockUI();
			//$('#form_empresa').data('formValidation').validate();
		},
		error: function(xhr, status, error) {
			terminarBlockUI();
			$('#contenido-modal').text('Error, no se pudo establecer la conexion.');
			$('#myModal').modal('show');
		}
	})
}



function realizarAbm(accion) {
    iniciarBlockUI(" Procesando...");
    $.ajax({
        type     : "post",
        dataType : "json",
        url      : accion,
        data     : $("#form_marca").serialize() + "&estado=" + $("#estado").is(':checked') ,
        success  : function(resultado) {
            if (resultado["resultado"] == "ok") {
                listado_marcas();
                $("#btnLimpiar").click();
            }
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

function validator() {
    $('#form_marca')
        .on('success.form.fv', function (e) {
            // si no hay errores ejecuto la accion segun el boton clickeado
            var $form = $(e.target),     // Form instance
            // Get the clicked button
                $button = $form.data('formValidation').getSubmitButton();

            // Prevent form submission
            e.preventDefault();
            switch ($button.attr('id')) {
                case 'btnRegistrar':
                    realizarAbm('/agregar_marca');
                    break;

                case 'btnModificar':
                    realizarAbm('/modificar_marca');
                    break;

                case 'btnEliminar':
                    realizarAbm('/eliminar_marca');
                    break;
            }
        })
        .formValidation({
            // I am validating Bootstrap form
            framework: 'bootstrap',

            // Feedback icons
            icon: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            err: {
                // You can set it to popover
                // The message then will be shown in Bootstrap popover
                container: 'tooltip'
            },
            // List of fields and their validation rules
            fields: {
                /*codigo: {
                    validators: {
                        notEmpty: {
                            message: 'El código de usuario es requerido y no puede estar vacío'
                        },
                        integer: {
                            message: 'El código de usuario debe ser un número positivo'
                        },
                    }
                },*/

                nombre: {
                    validators: {
                        notEmpty: {
                            message: 'El nombre es requerido y no puede estar vacio'
                        },
                        stringLength: {
                            min: 4,
                            max: 50,
                            message: 'El nombre debe tener mas de 4 y menos de 50 caracteres'
                        },
                        regexp: {
                            regexp: /^[a-z-A-Z-0-9\ \.\_\-\(\)\ñ\Ñ\,]*$/,
							message: 'Caracteres incorrectos'
                        }
                    }
                }
            }
        });
}