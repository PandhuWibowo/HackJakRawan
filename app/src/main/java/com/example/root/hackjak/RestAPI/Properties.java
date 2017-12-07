package com.example.root.hackjak.RestAPI;

import com.google.gson.annotations.SerializedName;

public class Properties{

	@SerializedName("kota")
	private String kota;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("kategori")
	private String kategori;

	@SerializedName("id")
	private int id;

	@SerializedName("kelurahan")
	private String kelurahan;

	@SerializedName("ipks")
	private double ipks;

	public void setKota(String kota){
		this.kota = kota;
	}

	public String getKota(){
		return kota;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setKategori(String kategori){
		this.kategori = kategori;
	}

	public String getKategori(){
		return kategori;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setKelurahan(String kelurahan){
		this.kelurahan = kelurahan;
	}

	public String getKelurahan(){
		return kelurahan;
	}

	public void setIpks(double ipks){
		this.ipks = ipks;
	}

	public double getIpks(){
		return ipks;
	}

	@Override
 	public String toString(){
		return 
			"Properties{" + 
			"kota = '" + kota + '\'' + 
			",kecamatan = '" + kecamatan + '\'' + 
			",kategori = '" + kategori + '\'' + 
			",id = '" + id + '\'' + 
			",kelurahan = '" + kelurahan + '\'' + 
			",ipks = '" + ipks + '\'' + 
			"}";
		}
}