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

        public Response<Inmueble> GuardarInmueble(Inmueble inmueble){
            try
            {
                context.Inmuebles.Add(inmueble);
                context.SaveChanges();
                return new Response<Inmueble>(inmueble);
            }
            catch (Exception e)
            {
                return new Response<Inmueble>($"Error de la aplicacion: {e.Message}");
            }
        }

        public List<Inmueble> Inmuebles(){
            return context.Inmuebles.ToList();
        }

        public List<fotoInmueble> Fotos(){
            return context.FotoInmuebles.ToList();
        }

        public Response<fotoInmueble> GuardarFotoInmueble(fotoInmueble foto){
            try
            {
                context.FotoInmuebles.Add(foto);
                context.SaveChanges();
                return new Response<fotoInmueble>(foto);
            }
            catch (Exception e)
            {
                return new Response<fotoInmueble>($"Error de la aplicacion: {e.Message}");
            }
        }

        public Response<fotoInmueble> Foto(string codigo)
        {
            fotoInmueble foto = context.FotoInmuebles.Find(codigo);
            if(foto == null){
                return new Response<fotoInmueble>("No existe");
            }
            return new Response<fotoInmueble>(foto);
        }
    }
}
