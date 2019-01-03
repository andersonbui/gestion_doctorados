/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "postgrado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Postgrado.findAll", query = "SELECT p FROM Postgrado p")
    , @NamedQuery(name = "Postgrado.findByIdPosgrado", query = "SELECT p FROM Postgrado p WHERE p.idPosgrado = :idPosgrado")
    , @NamedQuery(name = "Postgrado.findByNombrePostgrado", query = "SELECT p FROM Postgrado p WHERE p.nombrePostgrado = :nombrePostgrado")})
public class Postgrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_posgrado", nullable = false)
    private Integer idPosgrado;
    @Size(max = 45)
    @Column(name = "nombre_postgrado", length = 45)
    private String nombrePostgrado;
    @ManyToMany(mappedBy = "postgradoList")
    private List<Estudiante> estudianteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "postgrado")
    private Coordinador coordinador;

    public Postgrado() {
    }

    public Postgrado(Integer idPosgrado) {
        this.idPosgrado = idPosgrado;
    }

    public Integer getIdPosgrado() {
        return idPosgrado;
    }

    public void setIdPosgrado(Integer idPosgrado) {
        this.idPosgrado = idPosgrado;
    }

    public String getNombrePostgrado() {
        return nombrePostgrado;
    }

    public void setNombrePostgrado(String nombrePostgrado) {
        this.nombrePostgrado = nombrePostgrado;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPosgrado != null ? idPosgrado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Postgrado)) {
            return false;
        }
        Postgrado other = (Postgrado) object;
        if ((this.idPosgrado == null && other.idPosgrado != null) || (this.idPosgrado != null && !this.idPosgrado.equals(other.idPosgrado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Postgrado[ idPosgrado=" + idPosgrado + " ]";
    }
    
}
