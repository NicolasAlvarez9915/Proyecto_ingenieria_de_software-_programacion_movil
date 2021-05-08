using System;
using System.Collections.Generic;
using System.Linq;
using Datos;
using Entity;
using leco.Models;
using Logica;
using Microsoft.AspNetCore.Mvc;

namespace leco.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class InmuebleController : ControllerBase
    {
        private readonly InmuebleService Service;
        public InmuebleController(LecoContext context)
        {
            Service = new InmuebleService(context);
        }

        [HttpPost]
        public ActionResult<InmuebleViewModel> Post(InmuebleInputModel Input)
        {
            Inmueble inmueble = MapearInmueble(Input);
            var response = Service.GuardarInmueble(inmueble);
            return Ok(response.Object);
        }

        [HttpGet]
        public IEnumerable<InmuebleViewModel> Gets()
        {
            var inmuebles = Service.Inmuebles().Select(p => new InmuebleViewModel(p));
            return inmuebles;
        }

        [HttpGet("/InmueblesConFotoPrincipal")]
        public IEnumerable<InmuebleViewModel> GetInmuebleConFotoPrincipal()
        {
            var inmuebles = Service.InmueblesConFotoPrincipal().Select(p => new InmuebleViewModel(p)).ToList();
            return inmuebles;
        }

        

        [HttpGet("/Buscar/Inmueble/Direccion/{Direccion}")]
        public ActionResult<InmuebleViewModel> GetInmuebleDireccion(String Direccion)
        {
            var response = Service.BuscarInmueblePorDireccion(Direccion);
            if(response.Error){
                return BadRequest(response.Mensaje);
            }
            return new InmuebleViewModel(response.Object);
        }

        [HttpPost("/Foto")]
        public ActionResult<fotoInmuebleView> PostFotoInmueble(fotoInmuebleInput Input)
        {
            fotoInmueble foto = MapearFotoInmueble(Input);
            var response = Service.GuardarFotoInmueble(foto);
            return Ok(response.Object);
        }

        [HttpGet("Fotos/Todas")]
        public IEnumerable<fotoInmuebleView> GetFotos()
        {
            var fotos = Service.Fotos().Select(p => new fotoInmuebleView(p));
            return fotos;
        }

        [HttpGet("Fotos/Inmueble")]
        public IEnumerable<fotoInmuebleView> GetFotosInmueble(string codigo)
        {
            var fotos = Service.FotosInmueble(codigo).Select(p => new fotoInmuebleView(p));
            return fotos;
        }

        [HttpGet("Fotos/{codigo}")]
        public ActionResult<fotoInmuebleView> GetFoto(String codigo)
        {
            var response = Service.Foto(codigo);
            if (response.Error){
                return BadRequest(response.Mensaje);
            }
            return new fotoInmuebleView(response.Object);
        }

        private fotoInmueble MapearFotoInmueble(fotoInmuebleInput input)
        {
            return new fotoInmueble{
                Codigo = input.Codigo,
                CodInmueble = input.CodInmueble,
                Imagen = Convert.FromBase64String(input.Imagen)
            };
        }

        private Inmueble MapearInmueble(InmuebleInputModel input)
        {
            return new Inmueble{
                codigo = input.codigo,
                Descripcion = input.Descripcion,
                direccion = input.direccion,
                Estado = input.Estado,
                Nombre = input.Nombre,
                fotos = input.fotos.Select(f => MapearFotoInmueble(f)).ToList()
            };
        }

        [HttpPut("{codigo}")]
        public ActionResult<String> Put(InmuebleInputModel Input)
        {
            
            var mensaje = Service.ActualizarInmueble(Input);
            return Ok("Correcto");
        }
    }
}
