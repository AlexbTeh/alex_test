using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney
{
    public interface IBankRepository
    {
       Task<List<Valute>> getData();
    }
}
