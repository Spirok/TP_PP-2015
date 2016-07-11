function init() {
    
    listado_usuarios();
    select_perfiles();

    $("#select_usuario").change(function() {
        obtener_usuario();
        revalidate();
    });

    $("#btnLimpiar").click(function() {
        document.getElementById("select_usuario").selectedIndex =  -1;
        
        var fields = $('#form_usuario').data('formValidation').getOptions().fields,
            $parent, $icon;

        for (var field in fields) {
            $parent = $('[name="' + field + '"]').parents('.form-group');
            $icon   = $parent.find('.form-control-feedback[data-fv-icon-for="' + field + '"]');
            $icon.tooltip('destroy');
        }
        // Then reset the form
        $('#form_usuario').data('formValidation').resetForm(true);

        //document.getElementById("select_perfil").selectedIndex =  -1;
    });

    validator();
}



/*
 funcion que realiza una peticion post al sevlet /ListadoUsuarios el cual devuelve un objeto json con todos los
 usuarios existentes. Estos usuarios se rellenan en un combobox.
*/
function listado_usuarios() {
    iniciarBlockUI(" Cargando...");
    $.ajax({
        url: '/listado_usuarios',
        type: 'post',
        dataType: 'json',
        success : function(resultado) {
            // obtengo combobox
            combobox = document.getElementById("select_usuario");

            // seteo el largo del bombobox a 0
            combobox.options.length = 0;

            // obtengo todos los usuarios
            var usuarios = resultado.Records;
            for (var i = 0 ; i < usuarios.length ; i++) {
                //console.log(usuarios[i].nick);        
                var opcion = document.createElement("option");
                opcion.text = usuarios[i].nick;
                //opcion.value = usuarios[i].id;
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


function select_perfiles() {
    $.ajax({
        url: '/listado_perfiles',
        type: 'post',
        dataType: 'json',
        data: { accion : "lista_perfiles"},
        success: function (resultado) {
            // obtengo combobox
            combobox = document.getElementById("select_perfil");

            // seteo el largo del bombobox a 0
            combobox.options.length = 0;

            // obtengo todos los usuarios
            var perfiles = resultado.Records;
            for (var i = 0 ; i < perfiles.length ; i++) {
                //console.log(perfiles[i].id + " " + perfiles[i].descripcion);        
                var opcion = document.createElement("option");
                opcion.text  = perfiles[i].descripcion;
                opcion.value = perfiles[i].id;
                combobox.add(opcion);
            }
            // ninguna opcion seleccionada
            combobox.selectedIndex = -1;
        }
    });
}


function validator() {
    $('#form_usuario')
        .on('success.form.fv', function (e) {
            // si no hay errores ejecuto la accion segun el boton clickeado
            var $form = $(e.target),     // Form instance
            // Get the clicked button
                $button = $form.data('formValidation').getSubmitButton();

            // Prevent form submission
            e.preventDefault();
            switch ($button.attr('id')) {
                case 'btnRegistrar':
                    realizarAbm('/agregar_usuario');
                    break;

                case 'btnModificar':
                    realizarAbm('/modificar_usuario');
                    break;

                case 'btnEliminar':
                    realizarAbm('/eliminar_usuario');
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
                perfil:{
                    validators: {
                        notEmpty: {
                            message: 'El perfil es requerido y no puede estar vacio'
                        },
                    }
                },
                nick: {
                    validators: {
                        notEmpty: {
                            message: 'El nick es requerido y no puede estar vacio'
                        },
                        stringLength: {
                            min: 4,
                            max: 15,
                            message: 'El nick debe tener mas de 4 y menos de 30 caracteres'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_]+$/,
                            message: 'El nick solo puede contener valores alfanumericos'
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: 'La contrase&ntildea es requerida y no puede estar vacia'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_]+$/,
                            message: 'La contrase&ntildea solo puede contener valores alfanumericos'
                        }
                    }
                }
            }
        });
}

function realizarAbm(accion) {
    iniciarBlockUI(" Procesando...");
    $.ajax({
        type     : "post",
        dataType : "json",
        url      : accion,
        data     : $("#form_usuario").serialize() ,
        success  : function(resultado) {
            if (resultado["resultado"] == "ok") {
                listado_usuarios();
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

function obtener_usuario() {
    var datos =  { usuario_seleccionado : $("#select_usuario").val() };
    iniciarBlockUI(" Buscando Usuario...");
    $.ajax({
        url: '/info_usuario',
        type: 'post',
        dataType: 'json',
        data: datos,
        success : function(respuesta) {
            
            if (respuesta["resultado"] == "error") {
                $("#btnLimpiar").click();
                return;
            }
            console.log(respuesta);
            $("#codigo").val($.trim(respuesta.cod_usuario));
            $("#nick").val(respuesta.nick);
            $("#password").val(respuesta.password);
            $("#select_perfil").val(respuesta.id_perfil);
            
        },
        complete: function() {
            terminarBlockUI();
            revalidate();
        },
        error: function(xhr, status, error) {
            terminarBlockUI();
            $('#contenido-modal').text('Error, no se pudo establecer la conexion.');
            $('#myModal').modal('show');
        }
    })
}




function revalidate(){
    $('#form_usuario')
        //.formValidation('revalidateField', 'codigo')
        .formValidation('revalidateField', 'nick')
        .formValidation('revalidateField', 'password')
        .formValidation('revalidateField', 'perfil');
}