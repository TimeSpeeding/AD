//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Group13SSIS.Models
{
    using System;
    using System.Collections.Generic;
    
    public partial class Dept
    {
        public int DeptId { get; set; }
        public string Code { get; set; }
        public string Name { get; set; }
        public string ContactName { get; set; }
        public string Tel { get; set; }
        public string FaxNo { get; set; }
        public Nullable<int> HeadId { get; set; }
        public Nullable<int> RepId { get; set; }
        public Nullable<int> PointId { get; set; }
        public string Status { get; set; }
    }
}
