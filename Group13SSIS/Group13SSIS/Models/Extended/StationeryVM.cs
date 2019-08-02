using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using PagedList;

namespace Group13SSIS.Models
{
    public class StationeryVM
    {
        public IPagedList<Stationery> Stationeries { get; set; }
        public string Search { get; set; }
        public string Category { get; set; }
    }
}