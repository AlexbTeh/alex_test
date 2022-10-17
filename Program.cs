using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Reflection.Metadata;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Linq;
using System.Xml.Serialization;

namespace bnmmoney
{
    internal class Program
    {
        static void Main(string[] args)
        {

            var respository = new BankRepository();
            var valutes = respository.getData();
            foreach (var item in valutes.Result)
            {
                Console.WriteLine("Name " + item.Name + " Value " + item.Value + " Char code " + item.CharCode);
            }

        }
    }

}