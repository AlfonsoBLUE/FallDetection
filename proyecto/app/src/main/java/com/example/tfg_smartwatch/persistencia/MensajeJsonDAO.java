package com.example.tfg_smartwatch.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Esta clase representa un Data Access Object (DAO) que se utiliza para almacenar los datos en la base de datos.
 */
@Dao
public interface MensajeJsonDAO {

    /**
     * Metodo que devuelve todas las entradas de la base de datos.
     *
     * @return Lista con todos los mensajes en la base de datos
     */
    @Query("SELECT * FROM mensajejson")
    List<MensajeJSON> getAll();

    /**
     * Metodo que devuelve un mensaje almacenado en la base de datos a partir de su id.
     *
     * @param id Id del mensaje
     * @return Mensaje almacenado
     */
    @Query("SELECT * FROM mensajejson WHERE id = :id")
    MensajeJSON getById(int id);

    /**
     * Metodo que inserta un mensaje en la base de datos.
     *
     * @param mensaje
     */
    @Insert
    void insert(MensajeJSON mensaje);

    /**
     * Metodo que actualiza un mensaje que contiene la base de datos.
     *
     * @param mensaje
     */
    @Update
    void update(MensajeJSON mensaje);

    /**
     * Metodo que elimina un mensaje de la base de datos.
     *
     * @param mensaje
     */
    @Delete
    void delete(MensajeJSON mensaje);
}
