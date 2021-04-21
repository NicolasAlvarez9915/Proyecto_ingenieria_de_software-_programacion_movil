using System;

namespace Entity
{
    public class Response
    {
        public Object Object { get; set; }
        public bool Error { get; set; } 
        public String Mensaje { get; set; }

        public Response(Object Object)
        {
            Error = false;
            this.Object = Object;
        }
        public Response(string mensaje)
        {
            Error = true;
            this.Mensaje = mensaje;
        }
    }
}
