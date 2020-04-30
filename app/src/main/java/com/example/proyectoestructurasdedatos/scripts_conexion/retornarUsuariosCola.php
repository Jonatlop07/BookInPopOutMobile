<?php
include "conexion.php";

$consulta = "select nombre, id_documento, latitud, longitud from usuario join ubicacion on (usuario.id_ubicacion = ubicacion.id_ubicacion) limit 10";

$resultado = $conexion->query($consulta);

while ($fila = $resultado->fetch_array()) {
    $registro[] = array_map('utf8_encode', $fila);
}

echo json_encode($registro);

$resultado->close();
