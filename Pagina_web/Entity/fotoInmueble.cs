using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Entity
{
    public class fotoInmueble
    {
        [Key]
        [Column(TypeName = "nvarchar(11)")]
        public String Codigo { get; set; }
        [Column(TypeName = "nvarchar(11)")]
        public String CodInmueble { get; set; }
        public byte[] Imagen { get; set; }
    }
}
