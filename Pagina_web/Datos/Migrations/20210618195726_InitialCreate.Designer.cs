// <auto-generated />
using System;
using Datos;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

namespace Datos.Migrations
{
    [DbContext(typeof(LecoContext))]
    [Migration("20210618195726_InitialCreate")]
    partial class InitialCreate
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("ProductVersion", "5.0.5")
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("Entity.Inmueble", b =>
                {
                    b.Property<string>("codigo")
                        .HasColumnType("nvarchar(11)");

                    b.Property<string>("Descripcion")
                        .HasColumnType("nvarchar(500)");

                    b.Property<string>("Estado")
                        .HasColumnType("nvarchar(50)");

                    b.Property<string>("Nombre")
                        .HasColumnType("nvarchar(100)");

                    b.Property<string>("direccion")
                        .HasColumnType("nvarchar(100)");

                    b.HasKey("codigo");

                    b.ToTable("Inmuebles");
                });

            modelBuilder.Entity("Entity.fotoInmueble", b =>
                {
                    b.Property<string>("Codigo")
                        .HasColumnType("nvarchar(11)");

                    b.Property<string>("CodInmueble")
                        .HasColumnType("nvarchar(11)");

                    b.Property<byte[]>("Imagen")
                        .HasColumnType("varbinary(max)");

                    b.HasKey("Codigo");

                    b.HasIndex("CodInmueble");

                    b.ToTable("FotoInmuebles");
                });

            modelBuilder.Entity("Entity.fotoInmueble", b =>
                {
                    b.HasOne("Entity.Inmueble", null)
                        .WithMany("fotos")
                        .HasForeignKey("CodInmueble");
                });

            modelBuilder.Entity("Entity.Inmueble", b =>
                {
                    b.Navigation("fotos");
                });
#pragma warning restore 612, 618
        }
    }
}
