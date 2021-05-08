using System;
using System.Collections;
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
                inmueble.codigo = context.Inmuebles.ToList().Count().ToString();
                int codigoFoto = context.FotoInmuebles.ToList().Count();
                foreach (var foto in inmueble.fotos)
                {
                    foto.CodInmueble = inmueble.codigo;
                    foto.Codigo = codigoFoto++.ToString();
                }
                context.Inmuebles.Add(inmueble);
                context.SaveChanges();
                return new Response<Inmueble>(inmueble);
            }
            catch (Exception e)
            {
                return new Response<Inmueble>($"Error de la aplicacion: {e.Message}");
            }
        }
        
        public Response<Inmueble> BuscarInmueblePorDireccion(String direccion){
            Inmueble inmueble = context.Inmuebles.Where(u => u.direccion == direccion).FirstOrDefault();
            if (inmueble == null){
                return new Response<Inmueble>("No existe un inmueble con esta direccion.");
            }
            return new Response<Inmueble>(inmueble);
        }

        public List<Inmueble> Inmuebles(){
            return context.Inmuebles.ToList();
        }

        public List<Inmueble> InmueblesConFotoPrincipal(){
            List<Inmueble> inmuebles = context.Inmuebles.ToList();
            foreach (var iterador in inmuebles)
            {
                List<fotoInmueble> fotos = new List<fotoInmueble>();
                fotoInmueble foto = Foto(iterador.codigo).Object;
                fotos.Add(foto); 
                iterador.fotos = fotos;
            }
            return inmuebles;
        }

        public List<fotoInmueble> Fotos(){
            return context.FotoInmuebles.ToList();
        }

        public Response<fotoInmueble> GuardarFotoInmueble(fotoInmueble foto){
            try
            {
                foto.Codigo = context.FotoInmuebles.ToList().Count().ToString();
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
            fotoInmueble foto = context.FotoInmuebles.Where(u => u.CodInmueble == codigo).FirstOrDefault();
            if(foto == null){
                return new Response<fotoInmueble>("No existe");
            }
            return new Response<fotoInmueble>(foto);
        }

        public List<fotoInmueble> FotosInmueble(string codigo){
            return context.FotoInmuebles.Where(u => u.Codigo == codigo).ToList();
        }

        public Response<Inmueble> ActualizarInmueble(Inmueble inmuebleNuevo){
            try{
                var inmuebleViejo = context.Inmuebles.Find(inmuebleNuevo.codigo);
                if(inmuebleViejo != null){
                    inmuebleViejo.Nombre = inmuebleNuevo.Nombre;
                    inmuebleViejo.Estado = inmuebleNuevo.Estado;
                    inmuebleViejo.direccion = inmuebleNuevo.direccion;
                    inmuebleViejo.Descripcion = inmuebleNuevo.Descripcion;
                    context.Inmuebles.Update(inmuebleViejo);
                    context.SaveChanges();
                    return new Response<Inmueble>(inmuebleNuevo);
                }
                else{
                    return new Response<Inmueble>($"Lo sentimos, el Inmueble con dirección {inmuebleNuevo.direccion} no se encuentra registrado.");
                }
            }
            catch (Exception e){
                return new Response<Inmueble>($"Error de la Aplicación: {e.Message}");
            }
        }

    }
}
