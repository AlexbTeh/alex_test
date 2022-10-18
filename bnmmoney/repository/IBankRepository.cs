using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using bnmmoney.module;

namespace bnmmoney.repository
{
    public interface IBankRepository
    {
        Task<List<Valute>> GetData();
    }
}
