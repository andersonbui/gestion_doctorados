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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "estudiante", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usu_id"})
    , @UniqueConstraint(columnNames = {"est_id"})
    , @UniqueConstraint(columnNames = {"est_codigo"})
    , @UniqueConstraint(columnNames = {"est_cedula"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e")
    , @NamedQuery(name = "Estudiante.findByEstCedula", query = "SELECT e FROM Estudiante e WHERE e.estCedula = :estCedula")
    , @NamedQuery(name = "Estudiante.findByEstCodigo", query = "SELECT e FROM Estudiante e WHERE e.estCodigo = :estCodigo")
    , @NamedQuery(name = "Estudiante.buscarPorCodigoExceptoConId", query = "SELECT e FROM Estudiante e WHERE e.estCodigo = :estCodigo  and e.estId != :estId ")
    , @NamedQuery(name = "Estudiante.buscarPorCedulaExceptoConId", query = "SELECT e FROM Estudiante e WHERE e.estCedula = :estCedula  and e.estId != :estId ")
    , @NamedQuery(name = "Estudiante.findByEstCohorte", query = "SELECT e FROM Estudiante e WHERE e.estCohorte = :estCohorte")
    , @NamedQuery(name = "Estudiante.findByEstTutor", query = "SELECT e FROM Estudiante e WHERE e.estTutor = :estTutor")
    , @NamedQuery(name = "Estudiante.findByEstSemestre", query = "SELECT e FROM Estudiante e WHERE e.estSemestre = :estSemestre")
    , @NamedQuery(name = "Estudiante.findByEstCreditos", query = "SELECT e FROM Estudiante e WHERE e.estCreditos = :estCreditos")
    , @NamedQuery(name = "Estudiante.findByEstId", query = "SELECT e FROM Estudiante e WHERE e.estId = :estId")})
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_cedula", nullable = false)
    private int estCedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "est_codigo", nullable = false, length = 20)
    private String estCodigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_cohorte", nullable = false)
    private int estCohorte;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "est_tutor", nullable = false, length = 45)
    private String estTutor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_semestre", nullable = false)
    private int estSemestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_creditos", nullable = false)
    private int estCreditos;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "est_id", nullable = false)
    private Integer estId;
    @JoinTable(name = "estudiante_posgrado", joinColumns = {
        @JoinColumn(name = "est_id", referencedColumnName = "est_id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_posgrado", referencedColumnName = "id_posgrado", nullable = false)})
    @ManyToMany
    private List<Postgrado> postgradoList;
    @JoinColumn(name = "usu_id", referencedColumnName = "usu_id", nullable = false)
    @OneToOne(optional = false)
    private Usuario usuId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estId")
    private List<Documentacion> documentacionList;
    
    public Estudiante() {
    }

    public Estudiante(Integer estId) {
        this.estId = estId;
    }

    public Estudiante(Integer estId, int estCedula, String estCodigo, int estCohorte, String estTutor, int estSemestre, int estCreditos) {
        this.estId = estId;
        this.estCedula = estCedula;
        this.estCodigo = estCodigo;
        this.estCohorte = estCohorte;
        this.estTutor = estTutor;
        this.estSemestre = estSemestre;
        this.estCreditos = estCreditos;
    }

    public int getEstCedula() {
        return estCedula;
    }

    public void setEstCedula(int estCedula) {
        this.estCedula = estCedula;
    }

    public String getEstCodigo() {
        return estCodigo;
    }

    public void setEstCodigo(String estCodigo) {
        this.estCodigo = estCodigo;
    }

    public int getEstCohorte() {
        return estCohorte;
    }

    public void setEstCohorte(int estCohorte) {
        this.estCohorte = estCohorte;
    }

    public String getEstTutor() {
        return estTutor;
    }

    public void setEstTutor(String estTutor) {
        this.estTutor = estTutor;
    }

    public int getEstSemestre() {
        return estSemestre;
    }

    public void setEstSemestre(int estSemestre) {
        this.estSemestre = estSemestre;
    }

    public int getEstCreditos() {
        return estCreditos;
    }

    public void setEstCreditos(int estCreditos) {
        this.estCreditos = estCreditos;
    }

    public Integer getEstId() {
        return estId;
    }

    public void setEstId(Integer estId) {
        this.estId = estId;
    }

    @XmlTransient
    public List<Postgrado> getPostgradoList() {
        return postgradoList;
    }

    public void setPostgradoList(List<Postgrado> postgradoList) {
        this.postgradoList = postgradoList;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @XmlTransient
    public List<Documentacion> getDocumentacionList() {
        return documentacionList;
    }

    public void setDocumentacionList(List<Documentacion> documentacionList) {
        this.documentacionList = documentacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estId != null ? estId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.estId == null && other.estId != null) || (this.estId != null && !this.estId.equals(other.estId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.unicauca.proyectobase.entidades.Estudiante[ estId=" + estId + " ]";
    }

    }
