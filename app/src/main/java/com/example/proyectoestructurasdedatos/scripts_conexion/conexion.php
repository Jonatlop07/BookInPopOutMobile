<?php

$hostname = 'localhost';
$database = 'db_proyecto';
$username = 'root';
$password = '';

$conexion = new mysqli($hostname, $username, $password, $database);

if ($conexion->connect_errno) {
	echo "No se pudo realizar la conexion";
}
