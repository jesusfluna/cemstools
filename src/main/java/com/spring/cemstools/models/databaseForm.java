package com.spring.cemstools.models;

public class databaseForm {
	private String nombre;
	private int id;
	private boolean sustitucion;
	private boolean linealidad;
	private boolean replicacion;
	private boolean horarios;
	private int numEstaciones;
	private boolean unknown;
	
	public databaseForm() {
		this.nombre = "";
		this.id = 0;
		this.sustitucion = false;
		this.linealidad = false;
		this.replicacion = false;
		this.horarios = false;
		this.unknown = true;
		this.numEstaciones = 1;
	}
	
	
	public databaseForm(String nombre, int id, boolean sustitucion, boolean linealidad, boolean replicacion, boolean horarios,
			int numEstaciones, boolean unknown) {
		this.nombre = nombre;
		this.id = id;
		this.sustitucion = sustitucion;
		this.linealidad = linealidad;
		this.replicacion = replicacion;
		this.horarios = horarios;
		this.numEstaciones = numEstaciones;
		this.unknown = unknown;
	}
	
	
	public boolean isUnknown() {
		return unknown;
	}
	public void setUnknown(boolean unknown) {
		this.unknown = unknown;
	}
	public boolean isHorarios() {
		return horarios;
	}
	public void setHorarios(boolean horarios) {
		this.horarios = horarios;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isSustitucion() {
		return sustitucion;
	}
	public void setSustitucion(boolean sustitucion) {
		this.sustitucion = sustitucion;
	}
	public boolean isLinealidad() {
		return linealidad;
	}
	public void setLinealidad(boolean linealidad) {
		this.linealidad = linealidad;
	}
	public boolean isReplicacion() {
		return replicacion;
	}
	public void setReplicacion(boolean replicacion) {
		this.replicacion = replicacion;
	}
	public int getNumEstaciones() {
		return numEstaciones;
	}
	public void setNumEstaciones(int numEstaciones) {
		this.numEstaciones = numEstaciones;
	}

	@Override
	public String toString() {
		return "databaseForm [nombre=" + nombre + ", id=" + id + ", sustitucion=" + sustitucion + ", linealidad="
				+ linealidad + ", replicacion=" + replicacion + ", horarios=" + horarios + ", numEstaciones="
				+ numEstaciones + ", Unknown=" + unknown + "]";
	}

}
