using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Entity
{

    public class Inmueble
    {
        [Key]
        [Column(TypeName = "nvarchar(11)")]
        public String codigo { get; set; }
        [Column(TypeName = "nvarchar(100)")]
        public String Nombre { get; set; }  
        [Column(TypeName = "nvarchar(50)")]
        public String Estado { get; set; }
        [Column(TypeName = "nvarchar(100)")]
        public String direccion { get; set; }
        [Column(TypeName = "nvarchar(500)")]
        public String Descripcion { get; set; }
        [ForeignKey("CodInmueble")]
        public List<fotoInmueble> fotos { get; set; }
    }
}
