using System;
using Entity;

namespace leco.Models
{
    public class fotoInmuebleInput
    {
        public String Codigo { get; set; }
        public String CodInmueble { get; set; }
        public String Imagen { get; set; }
    }

    public class fotoInmuebleView : fotoInmuebleInput{
        public fotoInmuebleView()
        {

        }
        public fotoInmuebleView(fotoInmueble FotoInmueble)
        {
            Codigo = FotoInmueble.Codigo;
            CodInmueble = FotoInmueble.CodInmueble;
            Imagen = Convert.ToBase64String(FotoInmueble.Imagen);
        }
    }
}
