using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace leco.Models
{
    public class AutorizacionInputModel
    {
        public String FraseSecreta { get; set; }
    }

    public class AutorizacionViewModel
    {
        public String FraseSecreta { get; set; }
        public string Token { get; set; }
    }
}
