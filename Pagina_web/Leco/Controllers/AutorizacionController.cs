using Datos;
using Entity;
using leco.Config;
using leco.Models;
using leco.Service;
using Logica;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace leco.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class AutorizacionController: ControllerBase
    {
        private readonly AutorizacionService service;
        private readonly LecoContext lecoContext;
        private readonly JwtService jwtService;

        public AutorizacionController(LecoContext context, IOptions<AppSetting> appSettings)
        {
            lecoContext = context;
            var admin = lecoContext.Autorizacion.Find(1);
            if (admin == null)
            {
                lecoContext.Autorizacion.Add(new Autorizacion()
                {
                    FraseSecreta = "Frase Admin"                    
                }
                );
                var registrosGuardados = lecoContext.SaveChanges();
            }
            service = new AutorizacionService(context);
            jwtService = new JwtService(appSettings);
        }

        [AllowAnonymous]
        [HttpPost]
        public IActionResult Login([FromBody] AutorizacionInputModel model)
        {
            var user = service.Validate(model.FraseSecreta);
            if (user == null) return BadRequest("Frase secreta incorrecta");
            var response = jwtService.GenerateToken(user);
            return Ok(response);
        }

    }
}
