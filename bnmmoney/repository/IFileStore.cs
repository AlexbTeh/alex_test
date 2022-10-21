using bnmmoney.module;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    public interface IFileStore<T>
    {
        void WriteToXmlFile<ValCurs>(string filePath, ValCurs objectToWrite, bool append = false);

        ValCurs ReadFromXmlFile<ValCurs>(string filePath);
    }
}
