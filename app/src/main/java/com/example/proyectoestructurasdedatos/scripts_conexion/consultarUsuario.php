
<?php
include "conexion.php";

$id_documento = $_GET['id_documento'];

$consulta = "select nombre, apellido, correo from usuario where id_documento = $id_documento";

$resultado = $conexion->query($consulta);

$fila = $resultado->fetch_array();
$registro[] = array_map('utf8_encode', $fila);

echo json_encode($registro);

$resultado->close();
