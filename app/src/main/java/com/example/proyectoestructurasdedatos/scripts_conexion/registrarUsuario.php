<?php

include 'conexion.php';

$direccion = $_POST['DIRECCION'];

$insercion = "insert into direcciones(direccion) VALUES ('" . $direccion . "')";

mysqli_query($conexion, $insercion) or die(mysqli_error($conexion));
mysqli_close($conexion);
