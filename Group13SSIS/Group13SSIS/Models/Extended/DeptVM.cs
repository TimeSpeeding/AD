using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class DeptVM
    {
        public int DeptId { get; set; }
        [Display(Name = "Code:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter code!")]
        public string Code { get; set; }
        [Display(Name = "Department name:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter department name!")]
        public string Name { get; set; }
        [Display(Name = "Contact Name:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter contact name!")]
        public string ContactName { get; set; }
        [RegularExpression(@"^\d{3}\-\d{4}", ErrorMessage = "Please enter the number in accurate pattern!")]
        [Display(Name = "Telephone No:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter telephone no!")]
        public string Tel { get; set; }
        [RegularExpression(@"^\d{3}\-\d{4}", ErrorMessage = "Please enter the number in accurate pattern!")]
        [Display(Name = "Fax No:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter fax no!")]
        public string FaxNo { get; set; }
        [Display(Name = "Head:")]
        public string HeadId { get; set; }
        [Display(Name = "Representative:")]
        public string RepId { get; set; }
        [Display(Name = "Collection Point:")]
        public string PointId { get; set; }
    }
}