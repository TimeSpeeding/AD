using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class RetrievalVM
    {
        public Dictionary<string, Dictionary<int, Retrieval>> RetrievalDictionary { get; set; }
        public Dictionary<int, Retrieval> StationeryRetrievalDictionary { get; set; }
        public List<Retrieval> RetrievalList { get; set; }
        public class Retrieval
        {
            public int DeptId { get; set; }
            public int StationeryId { get; set; }
            public string Description { get; set; }
            public int NeededQty { get; set; }
            public int RetrievalQty { get; set; }
        }
    }
}