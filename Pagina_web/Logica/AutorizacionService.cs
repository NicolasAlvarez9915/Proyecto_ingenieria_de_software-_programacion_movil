using Datos;
using Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Logica
{
    public class AutorizacionService
    {
        private readonly LecoContext context;
        public AutorizacionService(LecoContext context)
        {
            this.context = context;
        }

        public Autorizacion Validate(string fraseSecreta)
        {
            List<Autorizacion> autorizacions = context.Autorizacion.Where(p => p.FraseSecreta == fraseSecreta).ToList();
            if (autorizacions.Count == 0) return null;
            return autorizacions[0];
        }
    }
}
