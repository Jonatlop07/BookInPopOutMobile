<?php

include 'conexion.php';

$id_usuario = $_POST['id_usuario'];
$id_documento = $_POST['id_documento'];
$nombres = $_POST['nombre'];
$apellidos = $_POST['apellido'];
$fecha_nacimiento = $_POST['fecha_nacimiento'];
$correo = $_POST['correo'];
$discapacitado = $_POST['discapacitado'];

$insercion = "insert into usuario values (" . $id_usuario . ", 1, " . $id_documento . ", '" . $nombres . "', '" . $apellidos . "', '" . $fecha_nacimiento . "', '" . $correo . "', " . $discapacitado . ")";


mysqli_query($conexion, $insercion) or die(mysqli_error($conexion));
mysqli_close($conexion);
