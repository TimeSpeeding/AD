using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace Group13SSIS.Models
{
    [MetadataType(typeof(CollectionPointValidation))]
    public partial class CollectionPoint
    {
    }
    public class CollectionPointValidation
    {
        public int PointId { get; set; }
        [Display(Name = "Name")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column Name can't be blank.")]
        public string Name { get; set; }
        [Display(Name = "Location")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column Location can't be blank.")]
        public string Location { get; set; }
    }
}