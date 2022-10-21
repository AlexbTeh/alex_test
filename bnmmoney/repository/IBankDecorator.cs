using bnmmoney.module;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    public interface IBankDecorator
    {
        Task<List<Valute>> getValutes();
    }
}
