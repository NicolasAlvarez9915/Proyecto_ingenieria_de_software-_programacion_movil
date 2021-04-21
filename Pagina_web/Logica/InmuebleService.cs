using System;
using System.Collections.Generic;
using System.Linq;
using Datos;
using Entity;

namespace Logica
{
    public class InmuebleService
    {
        private readonly LecoContext context;
        public InmuebleService(LecoContext context)
        {
            this.context = context;
        }

        public Response GuardarInmueble(Inmueble inmueble){
            try
            {
                context.Inmuebles.Add(inmueble);
                context.SaveChanges();
                return new Response(inmueble);
            }
            catch (Exception e)
            {
                return new Response($"Error de la aplicacion: {e.Message}");
            }
        }

        public List<Inmueble> Inmuebles(){
            return context.Inmuebles.ToList();
        }

        public List<fotoInmueble> Fotos(){
            return context.FotoInmuebles.ToList();
        }

        public Response GuardarFotoInmueble(fotoInmueble foto){
            try
            {
                context.FotoInmuebles.Add(foto);
                context.SaveChanges();
                return new Response(foto);
            }
            catch (Exception e)
            {
                return new Response($"Error de la aplicacion: {e.Message}");
            }
        }
        public object GuardarFotoInmueble(Inmueble inmueble)
        {
            throw new NotImplementedException();
        }
    }
}
