/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "documentacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentacion.findAll", query = "SELECT d FROM Documentacion d")
    , @NamedQuery(name = "Documentacion.findByDocIdentificador", query = "SELECT d FROM Documentacion d WHERE d.docIdentificador = :docIdentificador")
    , @NamedQuery(name = "Documentacion.findByIdEstudiante", query = "SELECT d FROM Documentacion d WHERE d.estId.estId = :estId")
    , @NamedQuery(name = "Documentacion.findByDocHash", query = "SELECT d FROM Documentacion d WHERE d.docHash = :docHash")
    , @NamedQuery(name = "Documentacion.findByDocDiropkm", query = "SELECT d FROM Documentacion d WHERE d.docDiropkm = :docDiropkm")
    , @NamedQuery(name = "Documentacion.findByDocCreditos", query = "SELECT d FROM Documentacion d WHERE d.docCreditos = :docCreditos")
    , @NamedQuery(name = "Documentacion.findByDocFechaVisado", query = "SELECT d FROM Documentacion d WHERE d.docFechaVisado = :docFechaVisado")
    , @NamedQuery(name = "Documentacion.findByDocFechaRegistro", query = "SELECT d FROM Documentacion d WHERE d.docFechaRegistro = :docFechaRegistro")
    , @NamedQuery(name = "Documentacion.findByDocEstado", query = "SELECT d FROM Documentacion d WHERE d.docEstado = :docEstado")
    , @NamedQuery(name = "Documentacion.findByDocFechaPublicacion", query = "SELECT d FROM Documentacion d WHERE d.docFechaPublicacion = :docFechaPublicacion")
    , @NamedQuery(name = "Documentacion.findByDocNumActa", query = "SELECT d FROM Documentacion d WHERE d.docNumActa = :docNumActa")
    , @NamedQuery(name = "Documentacion.findByDocVisado", query = "SELECT d FROM Documentacion d WHERE d.docVisado = :docVisado")
    , @NamedQuery(name = "Documentacion.findByDocAutoresSecundarios", query = "SELECT d FROM Documentacion d WHERE d.docAutoresSecundarios = :docAutoresSecundarios")
    , @NamedQuery(name = "Documentacion.StudentPublications_Year", query = "SELECT d FROM Documentacion d WHERE d.estId.estId = :identificador AND FUNC('YEAR',d.docFechaRegistro) = :anio ORDER BY  d.idTipoDocumentacion.nombre DESC")
    , @NamedQuery(name = "Documentacion.StudentPublications_Semester", query = "SELECT d FROM Documentacion d WHERE d.estId.estId = :identificador AND FUNC('YEAR',d.docFechaRegistro) = :anio AND FUNC('MONTH',d.docFechaRegistro) Between :inicio AND :fin ORDER BY  d.idTipoDocumentacion.nombre DESC")
    , @NamedQuery(name = "Documentacion.findAllByAnioFechaRegistro", query = "SELECT d FROM Documentacion d WHERE  FUNC('YEAR',d.docFechaRegistro) = :anio ORDER BY  d.idTipoDocumentacion.nombre DESC")
    , @NamedQuery(name = "Documentacion.findAllBySemestre", query = "SELECT d FROM Documentacion d WHERE  FUNC('YEAR',d.docFechaRegistro) = :anio AND FUNC('MONTH',d.docFechaRegistro) Between :inicio AND :fin ORDER BY  d.idTipoDocumentacion.nombre DESC")

})
public class Documentacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "doc_identificador", nullable = false)
    private Integer docIdentificador;
    @Size(max = 40)
    @Column(name = "doc_hash", length = 40)
    private String docHash;
    @Size(max = 30)
    @Column(name = "doc_diropkm", length = 30)
    private String docDiropkm;
    @Column(name = "doc_creditos")
    private Integer docCreditos;
    @Column(name = "doc_fecha_visado")
    @Temporal(TemporalType.DATE)
    private Date docFechaVisado;
    @Column(name = "doc_fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date docFechaRegistro;
    @Size(max = 15)
    @Column(name = "doc_estado", length = 15)
    private String docEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_fecha_publicacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date docFechaPublicacion;
    @Column(name = "doc_num_acta")
    private Integer docNumActa;
    @Size(max = 20)
    @Column(name = "doc_visado", length = 20)
    private String docVisado;
    @Size(max = 300)
    @Column(name = "doc_autores_secundarios", length = 300)
    private String docAutoresSecundarios;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "documentacion")
    private Libro libro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentacion")
    private List<Archivo> archivoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "documentacion")
    private Congreso congreso;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "documentacion")
    private Revista revista;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "documentacion")
    private CapituloLibro capituloLibro;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "documentacion")
    private Pasantia pasantia;
    @JoinColumn(name = "est_id", referencedColumnName = "est_id", nullable = false)
    @ManyToOne(optional = false)
    private Estudiante estId;
    @JoinColumn(name = "id_tipo_documentacion", referencedColumnName = "identificador", nullable = false)
    @ManyToOne(optional = false)
    private TipoDocumento idTipoDocumentacion;

    public Documentacion() {
    }

    public Documentacion(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public Documentacion(Integer docIdentificador, Date docFechaPublicacion) {
        this.docIdentificador = docIdentificador;
        this.docFechaPublicacion = docFechaPublicacion;
    }

    public Integer getDocIdentificador() {
        return docIdentificador;
    }

    public void setDocIdentificador(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public String getDocHash() {
        return docHash;
    }

    public void setDocHash(String docHash) {
        this.docHash = docHash;
    }

    public String getDocDiropkm() {
        return docDiropkm;
    }

    public void setDocDiropkm(String docDiropkm) {
        this.docDiropkm = docDiropkm;
    }

    public Integer getDocCreditos() {
        return docCreditos;
    }

    public void setDocCreditos(Integer docCreditos) {
        this.docCreditos = docCreditos;
    }

    public Date getDocFechaVisado() {
        return docFechaVisado;
    }

    public void setDocFechaVisado(Date docFechaVisado) {
        this.docFechaVisado = docFechaVisado;
    }

    public Date getDocFechaRegistro() {
        return docFechaRegistro;
    }

    public void setDocFechaRegistro(Date docFechaRegistro) {
        this.docFechaRegistro = docFechaRegistro;
    }

    public String getDocEstado() {
        return docEstado;
    }

    public void setDocEstado(String docEstado) {
        this.docEstado = docEstado;
    }

    public Date getDocFechaPublicacion() {
        return docFechaPublicacion;
    }

    public void setDocFechaPublicacion(Date docFechaPublicacion) {
        this.docFechaPublicacion = docFechaPublicacion;
    }

    public Integer getDocNumActa() {
        return docNumActa;
    }

    public void setDocNumActa(Integer docNumActa) {
        this.docNumActa = docNumActa;
    }

    public String getDocVisado() {
        return docVisado;
    }

    public void setDocVisado(String docVisado) {
        this.docVisado = docVisado;
    }

    public String getDocAutoresSecundarios() {
        return docAutoresSecundarios;
    }

    public void setDocAutoresSecundarios(String docAutoresSecundarios) {
        this.docAutoresSecundarios = docAutoresSecundarios;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @XmlTransient
    public List<Archivo> getArchivoList() {
        return archivoList;
    }

    public void setArchivoList(List<Archivo> archivoList) {
        this.archivoList = archivoList;
    }

    public Congreso getCongreso() {
        return congreso;
    }

    public void setCongreso(Congreso congreso) {
        this.congreso = congreso;
    }

    public Revista getRevista() {
        return revista;
    }

    public void setRevista(Revista revista) {
        this.revista = revista;
    }

    public CapituloLibro getCapituloLibro() {
        return capituloLibro;
    }

    public void setCapituloLibro(CapituloLibro capituloLibro) {
        this.capituloLibro = capituloLibro;
    }

    public Pasantia getPasantia() {
        return pasantia;
    }

    public void setPasantia(Pasantia pasantia) {
        this.pasantia = pasantia;
    }

    public Estudiante getEstId() {
        return estId;
    }

    public void setEstId(Estudiante estId) {
        this.estId = estId;
    }

    public TipoDocumento getIdTipoDocumentacion() {
        return idTipoDocumentacion;
    }

    public void setIdTipoDocumentacion(TipoDocumento idTipoDocumentacion) {
        this.idTipoDocumentacion = idTipoDocumentacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docIdentificador != null ? docIdentificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documentacion)) {
            return false;
        }
        Documentacion other = (Documentacion) object;
        if ((this.docIdentificador == null && other.docIdentificador != null) || (this.docIdentificador != null && !this.docIdentificador.equals(other.docIdentificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.unicauca.proyectobase.entidades.Documentacion[ docIdentificador=" + docIdentificador + " ]";
    }
    
}
