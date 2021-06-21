using System;
using Entity;
using Microsoft.EntityFrameworkCore;

namespace Datos
{
    public class LecoContext: DbContext
    {
        public LecoContext(DbContextOptions options): base(options){}
        public DbSet<Inmueble> Inmuebles { get; set; }
        public DbSet<fotoInmueble> FotoInmuebles { get; set; }
        public DbSet<Autorizacion> Autorizacion { get; set; }
    }
}
