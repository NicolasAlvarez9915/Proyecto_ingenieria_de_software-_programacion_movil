using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace Datos.Migrations
{
    public partial class InitialCreate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Inmuebles",
                columns: table => new
                {
                    codigo = table.Column<string>(type: "nvarchar(11)", nullable: false),
                    Nombre = table.Column<string>(type: "nvarchar(100)", nullable: true),
                    Estado = table.Column<string>(type: "nvarchar(50)", nullable: true),
                    direccion = table.Column<string>(type: "nvarchar(100)", nullable: true),
                    Descripcion = table.Column<string>(type: "nvarchar(500)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Inmuebles", x => x.codigo);
                });

            migrationBuilder.CreateTable(
                name: "FotoInmuebles",
                columns: table => new
                {
                    Codigo = table.Column<string>(type: "nvarchar(11)", nullable: false),
                    CodInmueble = table.Column<string>(type: "nvarchar(11)", nullable: true),
                    Imagen = table.Column<byte[]>(type: "varbinary(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_FotoInmuebles", x => x.Codigo);
                    table.ForeignKey(
                        name: "FK_FotoInmuebles_Inmuebles_CodInmueble",
                        column: x => x.CodInmueble,
                        principalTable: "Inmuebles",
                        principalColumn: "codigo",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateIndex(
                name: "IX_FotoInmuebles_CodInmueble",
                table: "FotoInmuebles",
                column: "CodInmueble");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "FotoInmuebles");

            migrationBuilder.DropTable(
                name: "Inmuebles");
        }
    }
}
