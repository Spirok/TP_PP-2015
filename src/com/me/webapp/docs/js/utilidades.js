

// ##############################################################################################################
// ##############################################################################################################

/* funcion generica usada en todas las secciones de la app. */
function initGenerico() {
	initLogeo();

}

// ##############################################################################################################
// ##############################################################################################################

// ----------------------------------------------------------------------------------------------

/* funcion que carga el header. Para eso debe existir un div id=header donde se llame. */
function cargarHeader() {
	console.log("cargando header...");
	$.ajax({
		url: '/PlantillaHtml',
		type: 'post',
		async : false,
		data: {ruta_archivo : "./src/com/me/webapp/docs/header.html"},
		success : function(data) {
			$("#header").html(data);
			console.log("header OK");
		}
	})
	.fail(function() {
		console.log("error");
	})
}

// ----------------------------------------------------------------------------------------------

/* funcion que carga la navbar. Para eso debe existir un div id=barra en el documento html donde se llame. */
function cargarNavbar() {
	console.log("cargando navbar...");
	$.ajax({
		url: '/PlantillaHtml',
		type: 'post',
		async : false,
		data: {ruta_archivo : "./src/com/me/webapp/docs/barra.html"},
		success : function(data) {
			$("#barra").html(data);
			console.log("navbar OK");
		}
	})
	.fail(function() {
		console.log("error");
	})
}

// ----------------------------------------------------------------------------------------------

/* funcion que carga la ventana modal. Para eso debe existir un div id=ventana_modal en el documento html donde
 se llame. */
function cargarModal() {
	console.log("cargando modal...");
	$.ajax({
		url: '/PlantillaHtml',
		type: 'post',
		async : false,
		data: {ruta_archivo : "./src/com/me/webapp/docs/modal.html"},
		success : function(data) {
			$("#ventana_modal").html(data);
			console.log("modal OK");
		}
	})
	.fail(function() {
		console.log("error");
	})
}

// ----------------------------------------------------------------------------------------------

// funcion que carga el footer. Para eso debe existir un div id=pie_pagina en el documento html donde se llame.
function cargarFooter() {
	console.log("cargando footer...");
	$.ajax({
		url: '/PlantillaHtml',
		type: 'post',
		async : false,
		data: {ruta_archivo : "./src/com/me/webapp/docs/footer.html"},
		success : function(data) {
			$("#pie_pagina").html(data);
			console.log("footer OK");
		}
	})
	.fail(function() {
		console.log("error");
	})
}


// ----------------------------------------------------------------------------------------------

/* funcion que retorna info json del usuario logeado en el sistema (nombre, id_perfil,
 nombre_perfil, cod_emp, nombre_empresa, email_empresa) */
function obtenerInfoSesion() {
	var info = {};
	$.ajax({
		url: '/DatosSesion',
		type: 'post',
		dataType: 'json',
		async    : false,
		success : function(data) {
			//console.log(data);
			info = data;
		}
	});
	return info;
}

// ----------------------------------------------------------------------------------------------
 

/* funcion que chequea si el usuario esta logeado, si es asi carga algunos contenidos html
(barra de navegacion, header, footer) y llegado el caso de que el usuario sea de tipo operador
oculta algunas opciones*/
function initLogeo() {
	var data = obtenerInfoSesion();
	// si no esta logeado redirige al login
	if (data.resultado == "error") {
		window.location.href = data.path;
		return;
	}
	if (data.resultado == "ok") {
		cargarHeader();
		cargarNavbar();
		cargarModal();
		cargarFooter();
		//console.log("setendo info usuario : " + data['nombre'] + " " + data['nombre_perfil']);
		$("#nombre_usuario").text(data['nombre']);
		$("#tipo_perfil").text(data['nombre_perfil']);
		if (data['id_perfil'] == 2) { // si es operador
			$("#navbar_admin_campos").hide();
			$("#navbar_usuarios").hide();
			if (data['nombre_empresa'].length != 0)  {
				$("#info_empresa").html("EMPRESA : <br>" + data['nombre_empresa']);
			}
		}
		if(data['id_perfil'] == 1) { // si es administrador
			$("#navbar_solicitud").hide();
		}
	}
}

// ----------------------------------------------------------------------------------------------

/* funcion que retorna el id_perfil del usuario logeado en el sistema. */
function obtenerIdPerfil() {
	var data = obtenerInfoSesion();
	return data['id_perfil'];
}

// ----------------------------------------------------------------------------------------------

/* funcion que retorna el codigo de empresa del usuario operador */
function obtenerCodEmpresaOperador() {
	var data = obtenerInfoSesion();
	return data["cod_emp"];
}


// ----------------------------------------------------------------------------------------------

/* funcion que retorna el nombre del usuario */
function obtenerNombreUsuario() {
	var data = obtenerInfoSesion();
	return data["nombre"];
}

// ----------------------------------------------------------------------------------------------

/* funcion que retorna el nombre del usuario */
function obtenerEmailOperador() {
	var data = obtenerInfoSesion();
	return data["email_empresa"];
}

// ----------------------------------------------------------------------------------------------


/* *funcion que retorna informacion de usuario operador (codigo_usuario, nick, codigo_empresa)
function obtenerInfoUsrOperador(nombreUsr) {
	var info = {};
	$.ajax({
		url: '/info_usuario_operador',
		type: 'post',
		data : {nombre_usuario : nombreUsr},
		dataType: 'json',
		success : function(data) {
			//console.log(data);
			info = data;
		}
	});
	return info;
}
*/

// ----------------------------------------------------------------------------------------------

/*
 funcion que realiza una peticion post al sevlet /listado_empresas_min el cual devuelve json con todos las
 empresas existentes. Estas empresas se rellenan en un combobox.
*/
function listado_empresas() {
    iniciarBlockUI(" Cargando...");
    $.ajax({
        url: '/listado_empresas_min',
        type: 'post',
        dataType: 'json',
        success : function(resultado) {
            // obtengo combobox
            combobox = document.getElementById("select_empresa");

            // seteo el largo del bombobox a 0
            combobox.options.length = 0;

            // obtengo todas las empresas
            var empresas = resultado.Records;
            for (var i = 0 ; i < empresas.length ; i++) {
                //console.log(empresas[i].nick);        
                var opcion   = document.createElement("option");
                opcion.value = empresas[i].codigo;
                opcion.text  = empresas[i].nombre;
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

// ----------------------------------------------------------------------------------------------

function iniciarBlockUI(msj) {
	//console.log("iniciando block");
	$.blockUI({
		message  : '<b><img width=16 height=16 src="../docs/images/cargando.gif" />' + msj +'</b>',
		fadeIn   : 500, 
		fadeOut  : 500,
		css      : { border: 'none', 
					padding: '5px', 
					'-webkit-border-radius': '10px', 
					'-moz-border-radius': '10px', 
					'border-radius': '10px',
					opacity: .8
				 },
	});
}

// ----------------------------------------------------------------------------------------------

function terminarBlockUI() {
	//console.log("terminando block");
	$.unblockUI();
}
