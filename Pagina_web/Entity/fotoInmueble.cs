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
    /*
        public fotoInmueble(String Codigo, String CodInmueble, String ImagenString){
            this.Codigo = Codigo;
            this.CodInmueble = CodInmueble;
            this.Imagen = Convert.FromBase64String(ImagenString);
        }

        public String ToString64(){
            return BitConverter.ToString(Imagen);
        }*/
    }
}
