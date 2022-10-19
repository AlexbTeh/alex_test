using bnmmoney.module;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    public interface IFileStore
    {
        void WriteToXmlFile<T>(string filePath, T objectToWrite, bool append = false) where T : new();

        T ReadFromXmlFile<T>(string filePath) where T : new();
    }
}
