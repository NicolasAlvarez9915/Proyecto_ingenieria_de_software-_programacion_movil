using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Entity
{
    public class Autorizacion
    {
        [Key]
        public int id { get; set; }
        [Column(TypeName = "nvarchar(200)")]
        public String FraseSecreta { get; set; }
    }
}
