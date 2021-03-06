using System;
using System.Collections.Generic;
using System.Linq;
using Entity;

namespace leco.Models
{
    public class InmuebleInputModel
    {
        public String codigo { get; set; }
        public String Nombre { get; set; } 
        public String Estado { get; set; }
        public String direccion { get; set; }
        public String Descripcion { get; set; }
        public List<fotoInmuebleInput> fotos { get; set; }
    }

    public class InmuebleViewModel: InmuebleInputModel{
        public InmuebleViewModel()
        {

        }
        public InmuebleViewModel(Inmueble inmueble)
        {
            codigo = inmueble.codigo;
            Nombre = inmueble.Nombre;
            Estado = inmueble.Estado;
            direccion = inmueble.direccion;
            Descripcion = inmueble.Descripcion;
            fotos = inmueble.fotos.Select(p => MapearFotoInmuebleView(p)).ToList();
        }
        private fotoInmuebleInput MapearFotoInmuebleView(fotoInmueble foto){
            return new fotoInmuebleInput{
                Codigo = foto.Codigo,
                CodInmueble = foto.CodInmueble,
                Imagen = Convert.ToBase64String(foto.Imagen)
            };
        }
    }
}
