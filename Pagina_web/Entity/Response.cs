using System;

namespace Entity
{
    public class Response<T>
    {
        public T Object { get; set; }
        public bool Error { get; set; } 
        public String Mensaje { get; set; }

        public Response(T Object)
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
